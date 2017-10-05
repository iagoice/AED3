import java.io.*;

class Teste{
   
   	public static void main(String[] args) throws Exception{
	   
   	 	Livro livro = new Livro();
	    Compra compra = new Compra();
	    Cliente cliente = new Cliente();
	    RandomAccessFile file = null;
	    RandomAccessFile file2 = null;
	    
	    try{
	 	      file = new RandomAccessFile("livraria.txt","rw");
	 	      file2 = new RandomAccessFile("Compras.txt", "rw");
	    
	    }catch(Exception e){
	       e.printStackTrace();
	    }
	    livro.setNome("efeito borboleta2");
	    livro.setAutor("jao brao2");
	    livro.setGenero("suspense");
	    livro.setPreco(5.00);
	    livro.cadastrarLivro(file);
	    Livro.mostrarLivros(file);
	    
	    
	    int array[] = {0,2,7};
	    compra.setItens(array);
	   compra.cadastrarCompra(file2);
	    
	    
	    
	    
	    
	    
	    
	    
   }

}