package Forming;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Cloon<T> {
	
	private T _ob;
	
	public Cloon(T ob) {
		this._ob = ob;
	}
	
	
	
	public T get_ob() {
		return _ob;
	}



	public void set_ob(T _ob) {
		this._ob = _ob;
	}



	@SuppressWarnings("unchecked")
	public T deepClone(){
		  try {
		        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		        objectOutputStream.writeObject(this._ob);
		        ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		        ObjectInputStream objectInputStream = new ObjectInputStream(bais);
		          return (T)objectInputStream.readObject();
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		      return null;
		    }
		  }
		}
