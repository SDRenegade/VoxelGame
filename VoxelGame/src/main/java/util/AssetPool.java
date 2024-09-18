package util;

import org.lwjgl.BufferUtils;
import renderer.BlockTextures;
import renderer.Shader;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL12.glTexImage3D;
import static org.lwjgl.opengl.GL12.glTexSubImage3D;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.stb.STBImage.*;

public class AssetPool {
    private static AssetPool instance;

    private Map<ShaderType, Shader> shaders;
    private int blockTextureArrayID;

    private AssetPool()
    {
        shaders = new EnumMap<>(ShaderType.class);
    }

    public void loadAssets()
    {
        // Loading shaders
        Shader defaultShader = new Shader(
                "assets/shaders/vDefault.glsl",
                "assets/shaders/fDefault.glsl");
        defaultShader.compile();
        shaders.put(ShaderType.DEFAULT, defaultShader);

        Shader uiShader = new Shader(
                "assets/shaders/vUI.glsl",
                "assets/shaders/fUI.glsl");
        uiShader.compile();
        shaders.put(ShaderType.UI, uiShader);

        Shader blockOutlineShader = new Shader(
                "assets/shaders/vBlockOutline.glsl",
                "assets/shaders/fBlockOutline.glsl");
        blockOutlineShader.compile();
        shaders.put(ShaderType.BLOCK_OUTLINE, blockOutlineShader);

        // Load Block Textures
        List<String> texturePaths = new ArrayList<>();
        for(BlockTextures values : BlockTextures.values())
            texturePaths.add(values.getTexturePath());
        createTextureArray(texturePaths, GL_TEXTURE0, false);

        // Load UI Textures
        texturePaths.clear();
        texturePaths.add("assets/textures/crosshairs.png");
        createTextureArray(texturePaths, GL_TEXTURE1, true);

        texturePaths = null;
    }

    private void createTextureArray(List<String> filePaths, int glTextureSlotID, boolean hasAlphaChannel)
    {
        int colorFormat = hasAlphaChannel ? GL_RGBA : GL_RGB;

        glActiveTexture(glTextureSlotID);

        blockTextureArrayID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D_ARRAY, blockTextureArrayID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameterf(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Since OpenGL expects the coordinates (0, 0) to be at the bottom left and images usually have
        // their origin at the top right, we need to tell stbi to vertically flip the texture.
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filePaths.get(0), width, height, channels, 0);
        glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, colorFormat, width.get(0), height.get(0), filePaths.size(), 0, colorFormat, GL_UNSIGNED_BYTE, GL_NONE);
        glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, 0, width.get(0), height.get(0), 1, colorFormat, GL_UNSIGNED_BYTE, image);
        for(int i = 1; i < filePaths.size(); i++) {
            image = stbi_load(filePaths.get(i), width, height, channels, 0);
            glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, width.get(0), height.get(0), 1, colorFormat, GL_UNSIGNED_BYTE, image);
        }

        // Freeing memory utilized by stbi library
        stbi_image_free(image);
    }

    public Shader getShader(ShaderType shaderType)
    {
        if(shaders.containsKey(shaderType))
            return shaders.get(shaderType);
        else {
            assert false : "ERROR: Shader for ShaderType '" + shaderType + "' not found.";
            return null;
        }
    }

    public int getBlockTextureArrayID() { return blockTextureArrayID; }

    public static AssetPool getInstance()
    {
        if(instance == null)
            instance = new AssetPool();

        return instance;
    }
}

