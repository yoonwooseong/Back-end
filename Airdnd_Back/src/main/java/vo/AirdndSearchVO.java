package vo;

public class AirdndSearchVO {
	private String sub_title, title;
	private int home_idx, filter_max_person, filter_bedroom, filter_bed, filter_bathroom, price;

	public AirdndSearchVO() {

	}

	public AirdndSearchVO(int home_idx, String sub_title, String title, int filter_max_person,
			int filter_bedroom, int filter_bed, int filter_bathroom, int price) {
		
		this.home_idx = home_idx;
		this.sub_title = sub_title;
		this.title = title;
		this.filter_max_person = filter_max_person;
		this.filter_bedroom = filter_bedroom;
		this.filter_bed = filter_bed;
		this.filter_bathroom = filter_bathroom;
		this.price = price;
	}


	public int getHome_idx() {
		return home_idx;
	}

	public void setHome_idx(int home_idx) {
		this.home_idx = home_idx;
	}

	public String getSub_title() {
		return sub_title;
	}

	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getFilter_max_person() {
		return filter_max_person;
	}

	public void setFilter_max_person(int filter_max_person) {
		this.filter_max_person = filter_max_person;
	}

	public int getFilter_bedroom() {
		return filter_bedroom;
	}

	public void setFilter_bedroom(int filter_bedroom) {
		this.filter_bedroom = filter_bedroom;
	}

	public int getFilter_bed() {
		return filter_bed;
	}

	public void setFilter_bed(int filter_bed) {
		this.filter_bed = filter_bed;
	}

	public int getFilter_bathroom() {
		return filter_bathroom;
	}

	public void setFilter_bathroom(int filter_bathroom) {
		this.filter_bathroom = filter_bathroom;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}


} 