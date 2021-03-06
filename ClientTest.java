package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;




public class ClientTest {
	
    private HttpServer server;

    @Before
    public void startaServidor() {
        this.server = Servidor.inicializaServidor();
    }

    @After
    public void mataServidor() {
        server.stop();
    }
	
    @Test
    public void testaQueBuscarUmCarrinhoTrasUmCarrinho() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080");
        String conteudo = target.path("/carrinhos/1").request().get(String.class);
        Carrinho fromXML = (Carrinho) new XStream().fromXML(conteudo);
        Assert.assertEquals("Rua Vergueiro 3185, 8 andar",fromXML.getRua());
    }

}
