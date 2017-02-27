import io.netty.handler.codec.http.HttpResponse;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import static java.net.InetAddress.getLocalHost;

public class AndroidProxy {
    private BrowserMobProxyServer server;
    private List<Response> responseList;
    private ExecutorService executorService;

    AndroidProxy(){
        server = new BrowserMobProxyServer();
    }

    public void startProxy() throws UnknownHostException {
        server.start(0, getLocalHost());
    }

    public void replaceJsonInResponse(final List<String> hostList, final List<String> jsonList){

        responseList = Collections.synchronizedList(new ArrayList<Response>());
        executorService = Executors.newFixedThreadPool(1);

        server.addResponseFilter(new ResponseFilter() {
            @Override
            public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {

                //бегу по масиву хостов
                for(int i = 0; i < hostList.size(); i++){
                    //Если хост совпадает делаю подмену jsona
                    if(messageInfo.getUrl().contains(hostList.get(i))){
                        contents.setTextContents(jsonList.get(i));
                        System.out.println(messageInfo.getUrl());
                        responseList.add(new Response(response, contents, messageInfo));
                    }
                }
            }
        });
    }

    public BrowserMobProxyServer getProxyServer(){return  server;}
    public List<Response> getResponseList() {
        return responseList;
    }
}