    #version 330
    layout (location=0) in vec3 aPos;
    layout (location=1) in float aColorFactor;

    out float fColorFactor;

    uniform mat4 view;
    uniform mat4 projection;

    void main()
    {
        gl_Position = projection * view * vec4(aPos, 1.0);
        fColorFactor = aColorFactor;
    }