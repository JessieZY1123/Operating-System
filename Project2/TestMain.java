
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class TestMain {

    public static void main(String[] args) {
        int num = 13;
        if(args.length >0){
            Scanner input=new Scanner(System.in);
            num = input.nextInt();
        }
        //Initialize the semaphore
        final Semaphore boyBathroom = new Semaphore(3); // boyBathroom has 3 resources
        final Semaphore girlBathroom = new Semaphore(3);// girlBathroom has 3 resources

        ClassRoom classRoom =  ClassRoom.INIT;
        List<StudentThread> studentThreadList = new ArrayList<>();
        StudentThread studentThread = null;
        for(int i=0;i<num;i++){
            if(i%2==1) {
              studentThread  = new StudentThread(i,boyBathroom, classRoom,"Boy");
            }else{
                studentThread  = new StudentThread(i,girlBathroom, classRoom,"Girl");
            }
            if(i%3==0){
                studentThread.setPriority(Thread.MAX_PRIORITY);
            }
            studentThreadList.add(studentThread);
        }
        TeacherThread teacherThread = new TeacherThread(classRoom,studentThreadList.get(0));

        for (int i = num-1;i>=0;i--){
            if(i==num-1){
                studentThreadList.get(i).setNextStudent(null);
            }else{
                studentThreadList.get(i).setNextStudent(studentThreadList.get(i+1));
            }
            teacherThread.getStudentWaitClassQueue().offer(studentThreadList.get(i));
        }
        

        Thread teacher = new Thread(teacherThread);
        teacher.start();

       
    }
}
