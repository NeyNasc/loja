package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;




public class ClientTest {
	
    private HttpServer server;
    private WebTarget target;
    private Client client;

    @Before
    public void startaServidor() {
        this.server = Servidor.inicializaServidor();
        ClientConfig config = new ClientConfig();
        config.register(new LoggingFilter());        
        this.client = ClientBuilder.newClient(config);
        this.target = client.target("http://localhost:8080");
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
    
    @Test
    public void testaQueSuportaNovosCarrinhos(){
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080");
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        String xml = carrinho.toXML();

        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

        Response response = target.path("/carrinhos").request().post(entity);
        //Assert.assertEquals("<status>sucesso</status>", response.readEntity(String.class));
        Assert.assertEquals(201, response.getStatus());
    }

}
