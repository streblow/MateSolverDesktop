/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matesolver;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import javax.swing.SwingUtilities;

/**
 *
 * @author streblow
 */
public abstract class AsyncTask <Params, Progress, Result> {
    protected AsyncTask() {
    }

    protected abstract void onPreExecute();

    protected abstract Result doInBackground(Params... params) ;

    protected abstract void onProgressUpdate(Progress... progress) ;

    protected abstract void onPostExecute(Result result) ;

    final void  publishProgress(final Progress... values) {
        ///*Java 1.8*/SwingUtilities.invokeLater(() -> this.onProgressUpdate(values) );
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                AsyncTask.this.onProgressUpdate(values);
            }
        });
    }

    final AsyncTask<Params, Progress, Result> execute(final Params... params) {
        // Invoke pre execute
        try {
            ///*Java 1.8*/SwingUtilities.invokeAndWait( this::onPreExecute );
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    AsyncTask.this.onPreExecute();
                }
            });
        } catch (InvocationTargetException|InterruptedException e){
            e.printStackTrace();
        }

        // Invoke doInBackground
        ///*Java 1.8*/CompletableFuture<Result> cf =  CompletableFuture.supplyAsync( () -> doInBackground(params) );
        new Thread(new Runnable() {
            public void run() {
                final Result result;
                result = AsyncTask.this.doInBackground(params);
                AsyncTask.this.onPostExecute(result);
            }
        }).start();

        // Invoke post execute
        ///*Java 1.8*/cf.thenAccept(this::onPostExecute);
        return this;
    }
}