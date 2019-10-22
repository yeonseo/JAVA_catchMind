package Model;

public class KeyWordVO {
	
	private int no;
	private String KeyWord;
			
	public KeyWordVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public KeyWordVO(String keyWord) {
		super();
		KeyWord = keyWord;
	}


	public KeyWordVO(int no, String keyWord) {
		super();
		this.no = no;
		KeyWord = keyWord;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getKeyWord() {
		return KeyWord;
	}

	public void setKeyWord(String keyWord) {
		KeyWord = keyWord;
	}
	
	
	
}
