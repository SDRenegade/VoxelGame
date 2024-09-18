    #version 330
    layout (location=0) in vec2 aPos;
    layout (location=1) in vec3 aTexCoord;

    out vec3 texCoord;

    uniform mat4 transformationMatrix;

    void main()
    {
        gl_Position = transformationMatrix * vec4(aPos, 0.0, 1.0);
        texCoord = aTexCoord;
    }