
package sample.toughpad.avcmms.com.webviewsample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.concurrent.Callable;

public class MyActivity extends Activity implements View.OnClickListener {

    private final static int TIME_OUT = 5000;
    private WebView mWebView;
    private boolean mNewPicture;
    private boolean mLoaded;
    private int mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final WebViewClient webViewClient = new WaitForLoadedClient(this);
        final WebChromeClient webChromeClient = new WaitForProgressClient(this);

        mWebView = (WebView) findViewById(R.id.web_page);
        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setPictureListener(new WaitForNewPicture());

        ((Button) findViewById(R.id.button1)).setOnClickListener(this);
        ((Button) findViewById(R.id.button2)).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    synchronized public void onNewPicture() {
        mNewPicture = true;
    }

    synchronized public void onPageFinished() {
        mLoaded = true;

    }

    synchronized public void onPageStarted() {
        mNewPicture = false;
    }

    synchronized public void onProgressChanged(int prog) {
        mProgress = prog;
    }

    @Override
    public void onClick(View v) {
        final MockWebViewClient client = new MockWebViewClient(this);
        mWebView.setWebViewClient(client);

        switch (v.getId()) {
            case R.id.button1:
                mWebView.loadUrl("file:///android_asset/webkit/test_hello_world.html");
                clearLoad();
                /*
                 * waitOnUiThread(5000, new Callable<Boolean>() {
                 * @Override public Boolean call() throws Exception { return
                 * isLoaded(); } });
                 */
                break;
            case R.id.button2:
                boolean b = mWebView.zoomIn();
                Log.i("koba", "zooom in is " + b);

                break;
        }

    }

    synchronized private void clearLoad() {
        mLoaded = false;
        mNewPicture = false;
        mProgress = 0;
    }

    private Boolean isLoaded() {
        return mLoaded && mNewPicture && mProgress == 100;
    }

    /**
     * A WebChromeClient used to capture the onProgressChanged for use in
     * waitFor functions. If a test must override the WebChromeClient, it can
     * derive from this class or call onProgressChanged directly.
     */
    public static class WaitForProgressClient extends WebChromeClient {
        private MyActivity activity;

        public WaitForProgressClient(MyActivity a) {
            activity = a;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            activity.onProgressChanged(newProgress);
        }
    }

    /**
     * A WebViewClient that captures the onPageFinished for use in waitFor
     * functions. Using initializeWebView sets the WaitForLoadedClient into the
     * WebView. If a test needs to set a specific WebViewClient and needs the
     * waitForCompletion capability then it should derive from
     * WaitForLoadedClient or call WebViewOnUiThread.onPageFinished.
     */
    public static class WaitForLoadedClient extends WebViewClient {
        private MyActivity activity;

        public WaitForLoadedClient(MyActivity a) {
            activity = a;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            activity.onPageFinished();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            activity.onPageStarted();
        }
    }

    private void waitOnUiThread(long timeout, final Callable<Boolean> doneCriteria) {
        new PollingCheck(timeout) {

            @Override
            protected boolean check() {
                try {
                    return doneCriteria.call();
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }
        }.run();
    }

    private class MockWebViewClient extends WaitForLoadedClient {
        private boolean mOnPageStartedCalled;
        private boolean mOnPageFinishedCalled;
        private boolean mOnLoadResourceCalled;
        private int mOnReceivedErrorCode;
        private boolean mOnFormResubmissionCalled;
        private boolean mDoUpdateVisitedHistoryCalled;
        private boolean mOnReceivedHttpAuthRequestCalled;
        private boolean mOnUnhandledKeyEventCalled;
        private boolean mOnScaleChangedCalled;
        private String mLastShouldOverrideUrl;

        public MockWebViewClient(MyActivity a) {
            super(a);
        }

        public boolean hasOnPageStartedCalled() {
            return mOnPageStartedCalled;
        }

        public boolean hasOnPageFinishedCalled() {
            return mOnPageFinishedCalled;
        }

        public boolean hasOnLoadResourceCalled() {
            return mOnLoadResourceCalled;
        }

        public int hasOnReceivedErrorCode() {
            return mOnReceivedErrorCode;
        }

        public boolean hasOnFormResubmissionCalled() {
            return mOnFormResubmissionCalled;
        }

        public boolean hasDoUpdateVisitedHistoryCalled() {
            return mDoUpdateVisitedHistoryCalled;
        }

        public boolean hasOnReceivedHttpAuthRequestCalled() {
            return mOnReceivedHttpAuthRequestCalled;
        }

        public boolean hasOnUnhandledKeyEventCalled() {
            return mOnUnhandledKeyEventCalled;
        }

        public boolean hasOnScaleChangedCalled() {
            return mOnScaleChangedCalled;
        }

        public String getLastShouldOverrideUrl() {
            return mLastShouldOverrideUrl;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mOnPageStartedCalled = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // assertTrue(mOnPageStartedCalled);
            // assertTrue(mOnLoadResourceCalled);
            mOnPageFinishedCalled = true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            // assertTrue(mOnPageStartedCalled);
            mOnLoadResourceCalled = true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mOnReceivedErrorCode = errorCode;
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            mOnFormResubmissionCalled = true;
            dontResend.sendToTarget();
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            mDoUpdateVisitedHistoryCalled = true;
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view,
                HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
            mOnReceivedHttpAuthRequestCalled = true;
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
            mOnUnhandledKeyEventCalled = true;
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            mOnScaleChangedCalled = true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mLastShouldOverrideUrl = url;
            return false;
        }
    }

    private class WaitForNewPicture implements WebView.PictureListener {
        @Override
        public void onNewPicture(WebView view, Picture picture) {
            MyActivity.this.onNewPicture();
        }
    }

}
