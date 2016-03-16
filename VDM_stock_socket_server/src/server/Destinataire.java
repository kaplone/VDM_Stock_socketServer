package server;

import java.util.ArrayList;

public class Destinataire extends Enregistrable {
    
	private ArrayList<String> tags;

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

}
