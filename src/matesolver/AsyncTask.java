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

    final void  publishProgress(Progress... values) {
        SwingUtilities.invokeLater(() -> this.onProgressUpdate(values) );
    }

    final AsyncTask<Params, Progress, Result> execute(Params... params) {
        // Invoke pre execute
        try {
            SwingUtilities.invokeAndWait( this::onPreExecute );
        } catch (InvocationTargetException|InterruptedException e){
            e.printStackTrace();
        }

        // Invoke doInBackground
        CompletableFuture<Result> cf =  CompletableFuture.supplyAsync( () -> doInBackground(params) );

        // Invoke post execute
        cf.thenAccept(this::onPostExecute);
        return this;
    }
}