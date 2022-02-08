
public enum  ClassRoom {
    //initial 0 ， class 1 ,break 2, class 3  end 4
    INIT("initial", 0), CLASS1("CS01", 1), BREAK1("break01", 2),
    CLASS2("CS02", 3),END("end",4);
    public  int classState;
    public String name;
    public int index;

     ClassRoom(String name,int index){
         this.name = name;
         this.index = index;
        classState =0;
    }

    public int getClassState() {
        return classState;
    }

    public void setClassState(int classState) {
        this.classState = classState;
    }

    public String getClassNameByIndex(int index){
        for (ClassRoom c : ClassRoom.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
