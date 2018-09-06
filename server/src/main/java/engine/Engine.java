package fr.ensma.lias.bimedia2018machinelearning.engine;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Singleton;

import fr.ensma.lias.bimedia2018machinelearning.buffering.BufferControl;
import fr.ensma.lias.bimedia2018machinelearning.csv.CSVControl;
import fr.ensma.lias.bimedia2018machinelearning.csv.CSVUsage;
import fr.ensma.lias.bimedia2018machinelearning.fraudchecker.RuleChecker;
import fr.ensma.lias.bimedia2018machinelearning.model.Buffer;
import fr.ensma.lias.bimedia2018machinelearning.model.Fraud;
import fr.ensma.lias.bimedia2018machinelearning.model.Transaction;
import fr.ensma.lias.bimedia2018machinelearning.prediction.Classifier;
import fr.ensma.lias.bimedia2018machinelearning.prediction.PredictionContext;
import fr.ensma.lias.bimedia2018machinelearning.prediction.Predictor;
import fr.ensma.lias.bimedia2018machinelearning.prediction.RandomForestClassifier;


/**
 * @author Lotfi IDDIR
 */
@Singleton
public class Engine {

	private Set<Transaction> transactions;

	private Set<Transaction> treatedTransactions;

	private Set<Buffer> buffers;

	private List<Fraud> frauds;

	private boolean stop;

	private boolean end;

	private int index;

	private Predictor predictor;

	private int minutesDuration;
	
	private long start = 0;

	private Map<Integer,Integer> pdvClusteres;
	
	private Timestamp realDate ;
	
	public Set<Transaction> getTransactions() {
		Set<Transaction> set = new TreeSet<Transaction>();
		int i = 0;
		for (Transaction elem : this.transactions) {
			if (i >= this.index)
				set.add(elem);
			i++;
		}
		return set;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Set<Buffer> getBuffers() {
		return buffers;
	}

	public void setBuffers(Set<Buffer> buffers) {
		this.buffers = buffers;
	}

	public List<Fraud> getFrauds() {
		return frauds;
	}

	public void setFrauds(List<Fraud> frauds) {
		this.frauds = frauds;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	private void setEnd(boolean end) {
		this.end = end;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Set<Transaction> getTreatedtransactions(int limit) {
		Set<Transaction> set = new TreeSet<Transaction>();
		if (limit > 0) {
			Iterator<Transaction> iterator = ((TreeSet<Transaction>) treatedTransactions).descendingIterator();
			for (int i = 0; i < limit; i++) {
				if (iterator.hasNext())
					set.add(iterator.next());
			}
		} else
			set = this.treatedTransactions;
		return set;
	}

	public void setTreatedtransactions(Set<Transaction> treatedtransactions) {
		this.treatedTransactions = treatedtransactions;
	}

	public Predictor getPredictor() {
		return predictor;
	}

	public void setPredictor(Predictor predictor) {
		this.predictor = predictor;
	}
	public Map<Integer,Integer> getPdvClusteres() {
	    return pdvClusteres;
	}

	public void setPdvClusteres(Map<Integer,Integer> pdvClusteres) {
	    this.pdvClusteres = pdvClusteres;
	}

	public Engine() {
		super();
		this.index = 0;
		this.buffers = new TreeSet<Buffer>();
		this.frauds = new ArrayList<Fraud>();
		this.transactions = new TreeSet<Transaction>();
		this.treatedTransactions = new TreeSet<Transaction>();
		this.stop = false;
		this.setEnd(true);
		CSVControl csv = new CSVControl();
		this.setTransactions(csv.loadTransactions());
		this.pdvClusteres = new HashMap<Integer,Integer>();
		String folder=System.getProperty("user.dir");
	    	folder+="/src/main/resources/";
		CSVUsage csvread = new CSVUsage(folder+"References/storesClustered.csv",';');
		    String [] line = null;
		    
		    try {
			while((line = csvread.getCsvreader().readNext())!= null)
			{
			this.pdvClusteres.put(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
			}
		    } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		this.predictor = new Predictor("FraudDetectionApp", "local[*]");
	}

	public void setMinutesDuration(int pMinutesDuration) {
		this.minutesDuration = pMinutesDuration;
	}
	
	public int getMinutesDuration() {
		return this.minutesDuration;
	}
	
	public boolean isStarted() {
		return (!this.end);
	}
	
	public void start() {
		String folder = System.getProperty("user.dir");
		System.out.println("started");
		this.setStop(false);
		int scale = 24 * 60 / 5;
		start = 0;
		long interval = 5 * 60 * 1000;
		long begin = System.currentTimeMillis();
		Iterator<Transaction> itr = this.transactions.iterator();
		RuleChecker ruler = new RuleChecker();
		Classifier dtc = new RandomForestClassifier();// Gotta generelize this with a a parameter
		PredictionContext ctx = new PredictionContext();
		ctx.setClassifier(dtc);
		ruler.setPredictor(this.predictor);
		ruler.initPredictor(ctx, folder + "/src/main/resources/models/myRandomForestClassificationModel1528106154111");
		
		Thread newThread = new Thread() {
			Transaction elem = null;
			boolean next = true;
			@Override
			public void run() {
				while (!stop && !end) {
					if (itr.hasNext()) {
						if (next)
							elem = itr.next();
						long t = (elem.getDateDebut().getTime() + 7200000) % 86400000;
						int timeToAppear = (int) ((t / scale) - start);
						//System.out.println("start = "+start);
						Engine.this.setRealDate(new Timestamp(start * scale-60*60*1000)) ;
						
						if (timeToAppear > 0) {
							//System.out.println(Engine.this.getRealDate());
							next = false;
							/*try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}*/
						}
						else
						{
							next = true;
							// Step 1: Ticket
							Engine.this.addIndex();
							Engine.this.treatedTransactions.add(elem);
							System.out.println(elem);
							BufferControl bufferizer = new BufferControl();

							// Step2: Update buffer
							bufferizer.bufferize(elem, interval, Engine.this);
							Buffer toVerify = Engine.this.getLastOccurence(elem.getPdv());
							// Step3 : verify fraud
							try {
								if (ruler.predict(toVerify, ctx) > 0) {
									Engine.this.addFraud(Engine.this.toFraud(toVerify));
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}	
					} else {
						Engine.this.setEnd(true);
						for (Fraud b : Engine.this.frauds) {
							System.out.println(b);
						}
					}
					start = (System.currentTimeMillis() - begin);
				}	
				Engine.this.setEnd(true);
			}
		};
		this.end = false;
		newThread.start();
	}
	
	public void stop() {
		this.setStop(true);
	}

	public void addIndex() {
		this.index++;
	}

	public void clearTransactions() {
		for (int i = 0; i < this.index; i++) {
			Transaction t = ((TreeSet<Transaction>) this.transactions).first();
			this.transactions.remove(t);
		}
		this.index = 0;
	}

	public Fraud toFraud(Buffer b) {
		Fraud fraud = new Fraud();
		fraud.setPdv(b.getPdv());
		fraud.setTime(b.getDateDebut().getTime());
		fraud.setLog("Fraude suspectée chez le commerce " + b.getPdv() +" à "
				+ new SimpleDateFormat("HH:mm").format(new Date(b.getDateDebut().getTime())));
		return fraud;
	}
	
	public void addFraud(Fraud fraud)
	{
	    boolean add= true;
	    for(Fraud elem : this.getFrauds())
	    {
		if(elem.getPdv()==fraud.getPdv())
		{
		    if(Math.abs(elem.getTime()-fraud.getTime())<30*60*1000)
		    {
			add=false;
		    }
		}
	    }
	    if(add)
		this.getFrauds().add(fraud);
	}
	public Buffer getLastOccurence(int PDV)
	{
	    Iterator<Buffer> itr = ((TreeSet<Buffer>) this.buffers).descendingIterator();
	    boolean found = false;
	    Buffer out=null;
	    while (!found && itr.hasNext())
	    {
		out=itr.next();
		if(out.getPdv()==PDV)
		    found=true;
	    }
	    return out;
	}

	public Timestamp getRealDate() {
		return realDate;
	}

	public void setRealDate(Timestamp realDate) {
		this.realDate = realDate;
	}
}
