package scenes;

public class SceneManager {
    private static SceneManager instance;
    private Scene currentScene;

    private SceneManager() {}

    public static void switchScene(int sceneIndex)
    {
        switch(sceneIndex) {
            case 0:
                getInstance().currentScene = new FirstScene();
                break;
        }

        if(getInstance().currentScene != null) {
            getInstance().currentScene.init();
            getInstance().currentScene.start();
        }
    }

    public static Scene getCurrentScene() { return getInstance().currentScene; }

    public static SceneManager getInstance()
    {
        if(instance == null)
            instance = new SceneManager();

        return instance;
    }
}
