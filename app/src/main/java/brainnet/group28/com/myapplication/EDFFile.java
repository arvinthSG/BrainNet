package brainnet.group28.com.myapplication;

class EDFFile {
    private String name;
    private String absolutePath;

    public EDFFile(String name, String absolutePath) {
        this.name = name;
        this.absolutePath = absolutePath;
    }


    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
