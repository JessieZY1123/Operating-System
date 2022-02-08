
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class TeacherThread implements Runnable{

    public Queue<StudentThread> studentWaitClassQueue;


    public ClassRoom classRoom;
    StudentThread studentThread;

    //Semaphores are initialized to zero
    final Semaphore isCalled = new Semaphore(0);
    final Semaphore enterClass = new Semaphore(0);
    final Semaphore doClass1 = new Semaphore(0);
    final Semaphore breakClass = new Semaphore(0);
    final Semaphore doClass2 = new Semaphore(0);


    public static long time = System.currentTimeMillis();

    public TeacherThread (ClassRoom room, StudentThread studentThread){
        this.studentThread = studentThread;
        this.classRoom = room;
        studentWaitClassQueue = new ArrayDeque<>();

    }
    @Override
    public void run() {
        try {
        	//Initialize each student in the queue so that their semaphore is 0, which means all students are waiting
        	 for (StudentThread student : studentWaitClassQueue) {
               student.setIsCalled(isCalled);
               student.setEnterClass(enterClass);
               student.setDoClass1(doClass1);
               student.setBreakClass(breakClass);
               student.setDoClass2(doClass2);
               
           }
            classRoom.setClassState(0);
            isCalled.release(); //Teacher release one resources. Teacher call one student
            for (StudentThread student : studentWaitClassQueue) {
                student.start();  
            }
            //ready to enter classroom
            Thread.sleep(new Random().nextInt(200));
            msg("The teacher enter classRoom!");
            enterClass.release(); //teacher enter the classroom and release the recourse to let student enter classroom
            Thread.sleep(100);
            classRoom.setClassState(1);  //teacher has the first class
            doClass1.release();    // The teacher told one of the students that the first class had begun
            Thread.sleep(550);
           
            classRoom.setClassState(2);  // take a break
            breakClass.release(); // The teacher told one of the students to have a break
            Thread.sleep(300);

            classRoom.setClassState(3); // teacher has the second class
            doClass2.release(); //The teacher told one of the students that the second class had begun
            Thread.sleep(550);

            classRoom.setClassState(4); // all classes are finished
            studentThread.getOrderGohome().acquire(); //The teacher wait for all the students to go home
            msg("The teacher goes to home！");
           
            
            System.out.println("Student Name  "
                    +  " Total Number of attended classes  "+"Class Name  "+"Period numer");

            Iterator<StudentThread> iterator = studentWaitClassQueue.iterator();
            
            while(iterator.hasNext()){
                StudentThread stThread= iterator.next();
                String totalNum = stThread.getReportMap().size()+"";
                String classNames =stThread.getReportMap().keySet().toString();
                String stPriods =stThread.getReportMap().values().toString();

                System.out.printf("%-12s %-20s %-10s %-11s\n",stThread.getStudentName(),
                        totalNum,classNames,stPriods);
            }
            return;


        }catch (Exception e){

        }


    }

    public void msg(String m) { System.out.println("["+(System.currentTimeMillis()-time)+"] "+": "+m);
    }

    public Queue<StudentThread> getStudentWaitClassQueue() {
        return studentWaitClassQueue;
    }

    public void setStudentWaitClassQueue(Queue<StudentThread> studentWaitClassQueue) {
        this.studentWaitClassQueue = studentWaitClassQueue;
    }
    
}
