    #version 330 core

    in vec3 texCoord;
    in vec4 color;

    out vec4 fragColor;

    uniform sampler2DArray textureArray;

    void main()
    {
        // Texture Array
        vec4 texColor;

        if(texCoord.z < 0) {
            texColor = color;
        }
        else {
            texColor = texture(textureArray, texCoord.xyz) * color;
        }

        fragColor = texColor;
    }