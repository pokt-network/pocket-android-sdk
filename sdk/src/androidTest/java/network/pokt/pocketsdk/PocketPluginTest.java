package network.pokt.pocketsdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;

import network.pokt.pocketsdk.exceptions.CreateQueryException;
import network.pokt.pocketsdk.interfaces.Codable;
import network.pokt.pocketsdk.interfaces.SendRequestCallback;
import network.pokt.pocketsdk.models.Query;
import network.pokt.pocketsdk.models.Transaction;
import network.pokt.pocketsdk.models.Wallet;
import network.pokt.pocketsdk.plugin.PocketTestPlugin;
import network.pokt.pocketsdk.plugin.TestConfiguration;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PocketPluginTest {

    PocketTestPlugin plugin;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("network.pokt.pocketsdk.test", appContext.getPackageName());
    }

    @Test
    public void testExecuteQuery() throws Exception {
        Query query = this.plugin.createQuery("1", null, null);
        Query response = this.plugin.executeQuery(query);
        assertEquals(response.isExecuted(), true);
        assertEquals(response.isError(), false);
        assertNotNull(response.getResult());
    }

    @Test
    public void testSendTransaction() throws Exception {
        Wallet wallet = this.plugin.createWallet("1", null);
        Transaction transaction = this.plugin.createTransaction(wallet,"1", null);
        Transaction response = this.plugin.sendTransaction(transaction);
        assertEquals(response.isSent(), true);
        assertEquals(response.isError(), false);
    }

    @Test
    public void testExecuteQueryAsync() throws Exception {
        Query query = this.plugin.createQuery("1", null, null);
        this.plugin.executeQueryAsync(new SendRequestCallback<Query>(query) {
            @Override
            public void onResponse(Query response, Exception exception) {
                assertEquals(exception, null);
                assertEquals(response.isExecuted(), true);
                assertEquals(response.isError(), false);
                assertNotNull(response.getResult());
            }
        });
    }

    @Test
    public void testSendTransactionAsync() throws Exception {
        Wallet wallet = this.plugin.createWallet("1", null);
        Transaction transaction = this.plugin.createTransaction(wallet,"1", null);
        this.plugin.sendTransactionAsync(new SendRequestCallback<Transaction>(transaction) {
            @Override
            public void onResponse(Transaction response, Exception exception) {
                assertEquals(exception, null);
                assertEquals(response.isSent(), true);
                assertEquals(response.isError(), false);
            }
        });
    }

    @Before
    public void setUp() throws Exception {
        TestConfiguration config = new TestConfiguration();
        this.plugin = new PocketTestPlugin(config);
    }

    @After
    public void tearDown() throws Exception {
        this.plugin = null;
    }
}
