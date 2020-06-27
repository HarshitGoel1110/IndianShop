package com.harshit.indianstore;

import android.os.AsyncTask;

import com.github.sendgrid.SendGrid;

import java.util.Hashtable;

class SendGridAsyncTask extends AsyncTask<Hashtable<String , String> , Void , String> {

    AsynResponse asynResponse = null;

    public SendGridAsyncTask(AsynResponse asynResponse) {
        this.asynResponse = asynResponse;
    }

    @Override
    protected String doInBackground(Hashtable<String, String>... hashtables) {
        Hashtable<String , String>  table = hashtables[0];
        SendGridCredentials sendGridCredentials = new SendGridCredentials();
        SendGrid sendGrid = new SendGrid(sendGridCredentials.getUserName() , sendGridCredentials.getPassword());
        sendGrid.addTo(table.get("to"));
        sendGrid.setFrom(table.get("from"));
        sendGrid.setSubject(table.get("subject"));
        sendGrid.setText(table.get("text"));
        String response = sendGrid.send();
        return response;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        asynResponse.processFinish(true);
    }

    public interface AsynResponse {
        void processFinish(Boolean output);
    }

}
