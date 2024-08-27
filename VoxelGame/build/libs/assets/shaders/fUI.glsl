    #version 330 core

    in vec3 texCoord;

    out vec4 fragColor;

    uniform sampler2DArray textureArray;

    void main()
    {
        // Texture Array
        vec4 texColor = texture(textureArray, texCoord.xyz);
        fragColor = texColor;

        // Color visual of texture coordinates
        //fragColor = vec4(fTexCoord, 0, 1);

        // Discarding transparent fragments under a given threshold
        /*vec4 texColor = texture(ourTexture, fTexCoord);
        if(texColor.a < 0.1)
            discard;
        fragColor = texColor;*/
    }