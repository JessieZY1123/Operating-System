
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class StudentThread extends Thread {

    //The student's ID
    private int studentId;
    //    The name of student
    private  String sName;

    boolean hasBathroom;	//if student has not go bathroom, it sets false.
    private  String sexName;
     Semaphore bathroom;  // It represents the number of bathroom

     Semaphore isCalled;  //  It represents the signal for the teacher to call the students to go bathroom
     Semaphore enterClass ;// It represents the students can enter the classroom
     Semaphore doClass1 ;//  Students have the first class
     Semaphore breakClass ; //Students have a break between two classes
     Semaphore doClass2 ; // Students have the second class
     
     Semaphore orderGohome; // Students have finished class and go home.
     
     boolean isInClass=false;

    ClassRoom classRoom;

    String msg;
    StudentThread nextStudent;
    Map<String,String> reportMap;


    public static long time = System.currentTimeMillis();

    public StudentThread(int studentId, Semaphore bathroom, ClassRoom classRoom, String sexName) {
        this.bathroom = bathroom;
        this.sName ="Student_"+studentId;
        this.studentId = studentId;
        this.classRoom = classRoom;
        hasBathroom = false;
        this.sexName = sexName;
        reportMap = new HashMap<>();
        orderGohome = new Semaphore(0); 
        
    }

	@Override
    public void run() {
        try {
            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
            printlnMsg("is completing a Health Questionnaire.");
            Thread.sleep(new Random().nextInt(10));
            printlnMsg("is commuting to school. ");
            Thread.sleep(new Random().nextInt(20));
            printlnMsg("is arrived at school. ");
  
               isCalled.acquire();  //wait for teacher called
               printlnMsg("is called by teacher."); 
               isCalled.release(); //release the resource for teacher can call next student
               //The initialization of each bathroom is initialized to 3 in the main method
               // Then this student to go bathroom.If bathroom<0 ,other students will wait in bathroom.queue
               bathroom.acquire(); 
               printlnMsg("is going "+getBathroomName()+" bathroom");
               Thread.sleep(100);
               printlnMsg(" finished washing hands");
               bathroom.release(); // when the student finished, (s)he out of the bathroom 

             //Enter classRoom
             enterClass();
             //Some students may miss the first class.
             //If they miss the first class, they will enter the second class
             if(0 == classRoom.getClassState()){
                 doFirstClass();
                 doBreak();
                 doSecondClass();
                 
             }else if( 2 == classRoom.getClassState()){
                 doBreak();
                 doSecondClass();
             }
             // Semaphore orderGohome is initialized to zero, all the students are waiting
             // if this student is the last student ,then going home and release the semaphore for other students.
            if(nextStudent==null){
                printlnMsg("is going home.");
                orderGohome.release();
                
            }else{ 
            	// if the student is not the last student,
            	//they will wait the next student to go home, then this student can go home.
                nextStudent.getOrderGohome().acquire();
                printlnMsg("is going home.");
                orderGohome.release();
            }
      

        }catch (Exception e){
            printlnMsg("==============="+e);

        }

    }
    
    public void enterClass(){
        try {
            while (!isInClass) {
                if (classRoom.getClassState() % 2 == 0) { // Enter the classroom when there is no class
                    enterClass.acquire();  // Teacher releases the resource, the student can enter the class one by one
                    printlnMsg("is entering classRoom.");
                    isInClass = true;
                    enterClass.release();
                    break;
                } else {
                    Thread.sleep(new Random().nextInt(10)); // walk around the campus
                }
            }
        }catch (Exception e){
            System.out.println("================="+e);
        }
    }
   // Teacher releases the resource, the student can have the first class one by one
    public void doFirstClass(){
        try {
            doClass1.acquire();
            if (classRoom.getClassState() % 2 == 1) {
                int i = 1;
                printlnMsg(getStudentName() + " has " + "NO." + i + " class.");
                String className = classRoom.getClassNameByIndex(classRoom.getClassState());
                reportMap.put(className, i + "");
            }
            doClass1.release();
            Thread.sleep(500);
        }catch(Exception e){
            System.out.println("================="+e);
        }
    }
    // Teacher releases the resource, the student can take a break one by one
    public void doBreak(){
        try {
            breakClass.acquire();
            printlnMsg(getStudentName() + " has some fun between break. ");
            breakClass.release();
            Thread.sleep(new Random().nextInt(300));
        }catch (Exception e){
            System.out.println("================="+e);
        }
    }
    // Teacher release the resource, the student can have the second class one by one
    public void doSecondClass(){
        try{
            doClass2.acquire();
            if (classRoom.getClassState() % 2 == 1) {
                int i = 2;
                printlnMsg(" has " + "NO." + i + " class.");
                String className = classRoom.getClassNameByIndex(classRoom.getClassState());
                reportMap.put(className, i + "");
            }
            doClass2.release();
            Thread.sleep(500);
        }catch (Exception e){
            System.out.println("================="+e);
        }
    }

    public void printlnMsg(String m) { System.out.println("["+(System.currentTimeMillis()-time)+"] "+getStudentName()+": "+m);
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return sName;
    }

    public void setStudentName(String name) {
        this.sName = name;
    }

    public String getMsg() {
        return msg;
    }
    public String getBathroomName() {
    	return sexName;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public StudentThread getNextStudent() {
        return nextStudent;
    }

    public void setNextStudent(StudentThread nextStudent) {
        this.nextStudent = nextStudent;
    }

    public Map<String, String> getReportMap() {
        return reportMap;
    }

    public void setReportMap(Map<String, String> reportMap) {
        this.reportMap = reportMap;
    }

    public Semaphore getBathroom() {
        return bathroom;
    }

    public void setBathroom(Semaphore bathroom) {
        this.bathroom = bathroom;
    }

    public Semaphore getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(Semaphore isCalled) {
        this.isCalled = isCalled;
    }

    public Semaphore getEnterClass() {
        return enterClass;
    }

    public void setEnterClass(Semaphore enterClass) {
        this.enterClass = enterClass;
    }

    public Semaphore getDoClass1() {
        return doClass1;
    }

    public void setDoClass1(Semaphore doClass1) {
        this.doClass1 = doClass1;
    }

    public Semaphore getBreakClass() {
        return breakClass;
    }

    public void setBreakClass(Semaphore breakClass) {
        this.breakClass = breakClass;
    }

    public Semaphore getDoClass2() {
        return doClass2;
    }

    public void setDoClass2(Semaphore doClass2) {
        this.doClass2 = doClass2;
    }

   
    public Semaphore getOrderGohome() {
        return orderGohome;
    }

    public void setOrderGohome(Semaphore orderGohome) {
        this.orderGohome = orderGohome;
    }

}
