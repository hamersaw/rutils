package com.bushpath.rutils.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadedCsvReader extends Reader<double[]> {
    protected BufferedReader in;
    protected String[] header;
    protected BlockingQueue<String> stringQueue;
    protected BlockingQueue<double[]> recordQueue;
    protected LineReader lineReader;
    protected Worker[] workers;

    public ThreadedCsvReader(String filename, int workerCount) throws Exception {
        // initialize BufferedReader
        this.in = new BufferedReader(new FileReader(filename));
        this.header = this.in.readLine().split(",");

        // initialize LineReader and Worker[]
        this.stringQueue = new ArrayBlockingQueue(4096);
        this.recordQueue = new ArrayBlockingQueue(4096);

        this.workers = new Worker[workerCount];
        for (int i=0; i<this.workers.length; i++) {
            this.workers[i] = new Worker();
            this.workers[i].start();
        }

        this.lineReader = new LineReader();
        this.lineReader.start();
    }

    public String[] getHeader() {
        return this.header;
    }

    @Override
    public double[] next() throws Exception {
        while (true) {
            double[] record = recordQueue.poll(50, TimeUnit.MILLISECONDS);
            if (record != null) {
                return record;
            }

            // if record == null check if everything is complete
            boolean cont = false;
            for (Worker worker : this.workers) {
                if (!worker.isShutdown()) {
                    cont = true;
                    break;
                }
            }

            if (!cont && this.recordQueue.isEmpty()) {
                return null;
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (this.in != null) {
            this.in.close();
        }
    }

    protected class LineReader extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    String line = in.readLine();
                    if (line == null) {
                        break;
                    }

                    while (!stringQueue.offer(line)) {}
                } catch (Exception e) {
                    e.printStackTrace(); // TODO - log
                }
            }

            // shutdown workers
            for (Worker worker : workers) {
                worker.shutdown();
            }
        }
    }

    protected class Worker extends Thread {
        protected boolean shutdown;

        public Worker() {
            this.shutdown = false;
        }

        @Override
        public void run() {
            while (!stringQueue.isEmpty() || !this.shutdown) {
                try {
                    // read next
                    String line = stringQueue.poll(50, TimeUnit.MILLISECONDS);
                    if (line == null) {
                        continue;
                    }

                    // parse record
                    String[] fields = line.split(",");
                    double[] record = new double[fields.length];
                    for (int i=0; i<fields.length; i++) {
                        record[i] = Double.parseDouble(fields[i]);
                    }

                    while (!recordQueue.offer(record)) {}
                } catch (InterruptedException e) {
                } catch (Exception e) {
                    e.printStackTrace(); // TODO - log
                }
            }
        }

        public boolean isShutdown() {
            return this.shutdown;
        }

        public void shutdown() {
            this.shutdown = true;
        }
    }
}
