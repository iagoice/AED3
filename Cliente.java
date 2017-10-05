import java.io.*;
public class Cliente {
	public String nomeCliente;
	public int id;
	public String endereco;
	public int cep;
	public String cpf;
	public char delete;
	
	public Cliente(){
		nomeCliente = "";
		id = -1;
		endereco = "";
		cep = -1;
		cpf = "";
		delete = ' ';
	}
	
	//set----------------------------------------	
	public void setName(String nomeIn){
		nomeCliente = nomeIn;
	}
	
	public void setID(int idIn){
		id = idIn;
	}
	
	public void setAddress(String addressIn){
		endereco = addressIn;
	}
	
	public void setCep(int cepIn){
		cep = cepIn;
	}
	
	public void setCpf(String cpfIn){
		cpf = cpfIn;
	}
	
	public void setDelete(char deleteIn){
		if(deleteIn == ' ' || deleteIn == '*')
			delete = deleteIn;
	}
	
	//get----------------------------------------
	public String getName(){
		return nomeCliente;
	}
	
	public int getID(){
		return id;
	}
	
	public String getAddress(){
		return endereco;
	}
	
	public int getCep(){
		return cep;
	}
	
	public String getCpf(){
		return cpf;
	}
	
	public char getDelete(){
		return delete;
	}
	
	
	
	public void cadastrarCliente(RandomAccessFile file){
		int codigo;
		try {
			if(file.length() == 0){
				file.writeInt(0);
			}
			file.seek(0);
			codigo = file.readInt();
			setID(codigo);
			file.seek(file.length());
			writeObject(file);
			file.seek(0);
			file.writeInt(codigo+1);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void deletarCliente(int codigo, RandomAccessFile file)throws IOException{
		long pos = 0;
		Livro livro = new Livro();
		boolean stop = false;
		if(file.length()!=0){
			file.seek(0);
			file.readInt();
			while(!stop && file.getFilePointer() < file.length()){
				pos = file.getFilePointer();
				livro.readObject(file);
				if(livro.getID() == codigo){
					livro.setDelete('*');
					stop = true;
				}//end if
			}
	
			file.seek(pos);
			livro.writeObject(file);
		}//end if principal
	}
	
	
	//objects----------------------------------------------
	
	public void writeObject(RandomAccessFile file)throws IOException{
		file.writeInt(id);
		file.writeUTF(nomeCliente);
		file.writeUTF(endereco);
		file.writeInt(cep);
		file.writeUTF(cpf);
	}//end writeobject
	
	public void readObject(RandomAccessFile file)throws IOException{
		if(file.getFilePointer() != file.length() && file.length()!=0){
			setID(file.readInt());
			setName(file.readUTF());
			setAddress(file.readUTF());
			setCep(file.readInt());
			setCpf(file.readUTF());
		}
	}//end readobject
	
	
}
