    #version 330
    layout (location=0) in vec3 aPos;
    layout (location=1) in vec3 aTexCoord;
    layout (location=2) in float aFaceID;
    layout (location=3) in float aAoID;

    out vec3 texCoord;
    out float shading;

    uniform mat4 view;
    uniform mat4 projection;

    const float[6] faceShading = float[6] (
        0.5, 0.8, // Front, Back
        0.8, 0.5, // Left, Right
        1.0, 0.5  // Top, Bottom
    );
    const float[4] aoValues = float[4] (
        1, 0.65, 0.40, 0.2
    );

    void main()
    {
        gl_Position = projection * view * vec4(aPos, 1.0);
        texCoord = aTexCoord;
        int faceIndex = int(aFaceID);
        int aoIndex = int(aAoID);
        shading = faceShading[faceIndex] * aoValues[aoIndex];
    }