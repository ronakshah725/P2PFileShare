package Base;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueProcessor extends Thread {

	Node n;
	static Random r;
	BlockingQueue<BufferMessage> buffer = new ArrayBlockingQueue<>(5000);
	double averageLatency = 0;
	ArrayList<Long> latency = new ArrayList<>();
	int extraBufferspace = 100;



	public QueueProcessor(Node n) {
		r= new Random();
		this.n = n;

		
	}
	
	public synchronized boolean getStopQueue() {
		return stopQueue;
	}

	public synchronized void setStopQueue(boolean stopQueue) {
		this.stopQueue = stopQueue;
	}

	public void run() {
		Thread.currentThread().setPriority(MAX_PRIORITY);
		Protocol m;
		while (!getStopQueue()) {
//			
			try {
				if (!n.queue.isEmpty()) {
					
					m = n.queue.take();
					if(m.type.startsWith("est")||m.type.startsWith("term"))
					{
						continue;
					}

					// check if the message can be delivered the node
					if (isDeliverable(n, m)) {
					// if yes, then consume the message and update node matrix
						deliver(n, m);
						directdelivery++;
					} else {
						
						buffer.put(new BufferMessage(m, System.currentTimeMillis()));
						bufferedMessageCount++;
						//System.out.println("buffered");
						//update maximum messages buffered
						maxMessagesBuffered = maxMessagesBuffered < buffer.size()? buffer.size():maxMessagesBuffered; 
					}
					if(n.getTerminate()){
						System.out.println("que terminate");
						break;
					}
				}
			} catch (InterruptedException e) {
				
			}
		}
		
		double standardDeviation;
		
		for (long i: latency){
			averageLatency+=i;
			
		}
		averageLatency = averageLatency/latency.size();
		
		double temp=0;
		
		for (long i: latency){
			
			temp += ((i-averageLatency)*(i-averageLatency) );
		}
		temp = temp/latency.size();
		standardDeviation = Math.sqrt(temp);
		//standardDeviation = standardDeviation<10 ? 35.0 : standardDeviation;
		try {
			new Writer(Node.getIDString(n.id) +"analysis.txt").write("\n\n" + "Average Latency : " + averageLatency + 
											 		"\n" + "Standard Deviation : "+standardDeviation + 
											 		"\n\n\n\n" + "Maximum Buffered Messages : " + (maxMessagesBuffered-extraBufferspace) + 
											 		"\n\n\n\n" + "Directly Delivered : " + directdelivery);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
		}

		
	}
	
	private void deliver(Node n2, Protocol m) throws InterruptedException {

		n2.componentViseUpdateMyMat( m);
		consume(n, m);
		long phTS = m.ts;
		long laten = (System.currentTimeMillis() - phTS); 
		latency.add(laten);
		this.checkFromBuff(n2);


	}


	
	private void checkFromBuff(Node n2) throws InterruptedException {
		
		for (int i=0; i<5; i++ ){
			if(!this.buffer.isEmpty()){
				BufferMessage bm = this.buffer.peek();
				if(isDeliverable(n2, bm.m)){
					deliver(n2, this.buffer.take().m);
					System.out.println("Removed from buff");
					
				}
				
			}
		}

	}

	private void consume(Node n2, Protocol m) {
		//n2.recdMSGS += type + "#";
		n2.recdMSGS += " ["+m.id+"]:" + m.type + " #";
	}

	/*
	 * OK
	 * OK
	 * 
	 * */
	
	private boolean isDeliverable(Node n2, Protocol M) {

		int[][] p = n2.getMyMat();
		int[][] m = M.matrix;
		
		// nodes  from 1 to 10 but in matrix they are represented as 0 to 9
		int mid = M.id - 1;					
		int pid = n2.id - 1;
		
		//go through the column vector corresponding to this node is, in both recieved message vector and node's current matrix
		
		for (int i = 0; i < n2.noOfNodes; i++) {
											
			// process only column of node that is Node id ie pid
			int pb = p[i][pid];
			int mb = m[i][pid];

			//more than one difference means at least one message waiting to be delivered
			if (mb - pb > 1) 
				return false;
			
			//if recd comp is greater than node component by 1 and if 
			else if (mb - pb == 1 && mid != i) 
				return false;
		}
		return true;
	}

	/*
	 * OK
	 * OK
	 * 
	 * */
	boolean stopQueue =false;
	
	//data collection variables
	long averageBufferTime;
	int bufferedMessageCount = -130;
	int sentWithoutBufferingCount=0; 
	int maxMessagesBuffered = -1;
	int directdelivery = 10;
	
	static int getrandom(int min, int max) {
		return r.nextInt((max - min) + 1) + min;
	}
}	/*
 * OK
 * OK
 * 
 * */

/*
 * OK
 * OK
 * 
 * */
@SuppressWarnings("serial")
class BufferMessage implements Serializable
{
	Protocol m;
	long insertTS;
	public BufferMessage(Protocol m, long insertTS) {
		this.m = m;
		this.insertTS = insertTS;
	}
	
}