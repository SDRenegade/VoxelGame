    #version 330
    layout (location=0) in vec2 aPos;
    layout (location=1) in vec3 aTexCoord;
    layout (location=2) in vec4 aColor;

    out vec3 texCoord;
    out vec4 color;

    uniform mat4 transformationMatrix;

    void main()
    {
        texCoord = aTexCoord;
        color = aColor;
        gl_Position = transformationMatrix * vec4(aPos, 0.0, 1.0);
    }