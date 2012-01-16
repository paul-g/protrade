package org.ic.tennistrader.service.threads;

import org.apache.log4j.Logger;

public abstract class StoppableThread extends Thread{

    protected boolean stop = false;

    @Override
    public void run() {
        final Logger log = Logger.getLogger(this.getClass());
        log.info("Starting " + this.getClass());
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while(!stop){
            runBody();
        } 
        log.info("Stopped " + this.getClass());
    }

    protected abstract void runBody();

    public void setStop() {
    	this.stop = true;
    }
}
