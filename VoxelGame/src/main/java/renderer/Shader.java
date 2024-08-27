package renderer;

import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
    private String vertexSrc;
    private String fragmentSrc;
    private String vertexFilepath;
    private String fragmentFilepath;

    public Shader(String vertexFilepath, String fragmentFilepath)
    {
        this.vertexFilepath = vertexFilepath;
        this.fragmentFilepath = fragmentFilepath;

        try {
            String src = new String(Files.readAllBytes(Paths.get(vertexFilepath)));
            vertexSrc = src;
        }
        catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader:" + vertexFilepath;
        }

        try {
            String src = new String(Files.readAllBytes(Paths.get(fragmentFilepath)));
            fragmentSrc = src;
        }
        catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader:" + fragmentFilepath;
        }
    }

    public void compile()
    {
        int vertexID, fragmentID;

        // Load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + vertexFilepath + "\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "Error: Could not compile vertex shader:" + vertexFilepath;
        }

        // Load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + fragmentFilepath + "\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "Error: Could not compile fragment shader:" + fragmentFilepath;
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + vertexFilepath + " and " + fragmentFilepath + "\n\tShader linking failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "Error: Could not link shaders: " + vertexFilepath + " and " + fragmentFilepath;
        }

        // Now that the shaders are linked to the shader program, we have no need
        // for the shaders anymore, so they should be deleted.
        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }

    public void use()
    {
        // Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach()
    {
        glUseProgram(0);
    }

    public void uploadBool(String uniformName, boolean value)
    {
        glUniform1i(glGetUniformLocation(shaderProgramID, uniformName), value ? 1 : 0);
    }

    public void uploadInt(String uniformName, int value)
    {
        glUniform1i(glGetUniformLocation(shaderProgramID, uniformName), value);
    }

    public void uploadFloat(String uniformName, float value)
    {
        glUniform1f(glGetUniformLocation(shaderProgramID, uniformName), value);
    }

    public void uploadMatrix4f(String uniformName, Matrix4f matrix)
    {
        glUniformMatrix4fv(glGetUniformLocation(shaderProgramID, uniformName), false, matrix.get(new float[16]));
    }

    public int getShaderProgramID() { return shaderProgramID; }
}
