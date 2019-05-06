//SCS 2106 Assignment 4 Takehome Assignment - 16001184 - A.Rangoda

import java.util.Scanner;
//Initializing Block class to represent each memory block
class Block{
    int startId = 0;
    int endId;
    int processId;
    int data;
    boolean isFree=true;
    Block next=null;
    
    //For each memory block, the process id and the size of the data block is given
    Block(int pId, int data){
        this.data=data;
        this.processId=pId;
        this.endId=this.startId+this.data-1;
        
    }
}

//For this question I have initialized a linked list data structure to maintain the memory space of 25000 bytes.

class Memory{
    int memorySize;
    int newBlock;

    //Allocating a memory space for the head of the linked list by creating head memory object
    //Currently assigned 0 bytes for the head, and the processId will be P0
    Block head=new Block(0, 0);
    Block part=head;

    //Function which represent the whole memory
    Memory(int memorySize){
        this.memorySize=memorySize;
        head.isFree=false;
        Block temp=new Block(-1,memorySize+1);
        temp.startId=head.endId+1;
        head.next=temp;
    }

    //function like malloc
    void MyMalloc(int pId, int data){
        int space;
        int temp;
        //Initializing a minimum space variable in order to calculate the best fit algorithm
        int minSpace=99999;
        
        //Currently accessing memory object
        Block currentBlock=head;

        //Finding the best fitted memory block
        while(currentBlock!=null){
            if(currentBlock.isFree==true){
                space = (currentBlock.endId+1)-currentBlock.startId;
                if(data<=space){
                    temp=space-data;
                    if(temp<minSpace){
                        minSpace=temp;
                    }
                }
                
            }
            currentBlock=currentBlock.next;
        }

        currentBlock=head;
        
            while(currentBlock!=null){
                //Checking whether the currently accessing block is free or not
                if(currentBlock.isFree==true){
                    space = (currentBlock.endId+1)-currentBlock.startId;
                    //Allocating memory for the best fitted block
                    if(space-data==minSpace){  
                        Block newBlock=new Block(-1,data);
                        newBlock.next=currentBlock.next;
                        currentBlock.next=newBlock;
                        newBlock.endId=currentBlock.endId;
                        currentBlock.endId=currentBlock.startId+data-1;
                        currentBlock.isFree=false;
                        currentBlock.processId=pId;
                        newBlock.startId=currentBlock.endId+1;
                        break;
                    }
                }
                currentBlock=currentBlock.next;
                //Prints an error message if the available memory space is not enough to fit in
                if(currentBlock==null){
                    System.out.println("Not enough memory space for Process P"+pId);
                }
            }
    }

    //function like free
    void MyFree(int pId){
        part=head;
        boolean found = false;
        while(part.next!=null){
            if(part.processId == pId){
                part.isFree = true; 
                part.processId = -1;
                found = true;
            }
            part = part.next;
        }
        //Prints an error message if the memory block trying to free is not exist
        if(found==false){
            System.out.println("Process P"+pId+" does not exist");
        }
        part=head;
        while(part.next != null){
            if(part.isFree == true && part.next.isFree == true){
                part.endId=part.next.endId;
                part.next=part.next.next;
                continue;
            }
            else{
                part=part.next;
            } 
        }
    }

    //Function which prints the memory 
    void printMemory(){
        Block currP=head;
        System.out.println();
        System.out.println("Start Id      End Id\t   Process Id\n");
        while(currP != null){
            //if(currP.processId!=-1){
            if(currP.processId!=-1){
                System.out.println(currP.startId+"k\t  -   "+currP.endId+"k\t : P"+currP.processId);
            }
            else if(currP.startId<currP.endId){
                System.out.println(currP.startId+"k\t  -   "+currP.endId+"k\t : Free Space");
            }
            currP = currP.next;
        }
    }
}

class Main{
    public static void main(String args[]){
        //initializing a memory space of 25000 bytes
        Memory m=new Memory(25000);

        //Taking user inputs to determine process id and size of each memory block
        Scanner scan=new Scanner(System.in);
        String s;
        String[] x=new String[2];
        while(true){
            s=scan.nextLine();
            //Terminates the process when user inputs 'x' in the terminal
            if(s.contains("x")){
                break;
            }
            
            if(s.contains(" ")){
                x=s.split(" ");
                m.MyMalloc(Integer.parseInt(x[0]),Integer.parseInt(x[1]));
            }
            else{
                    m.MyFree(Integer.parseInt(s));
            }  
        }

        //Printing the memory stack
        m.printMemory();



        //When hard coding processes(Sample)

        // m.MyMalloc(1, 6000);
        // m.MyMalloc(2, 2000);
        // m.MyMalloc(3, 3000);
        // m.MyMalloc(4, 1000);
        // m.MyMalloc(5, 3000);
        // m.MyFree(2);
        // m.MyFree(4);
        // m.MyMalloc(6, 1000);
        // m.MyMalloc(7, 1000);
        // m.printMemory();
    }
}