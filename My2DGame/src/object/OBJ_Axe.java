package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity{

	public OBJ_Axe(GamePanel gp) {
		super(gp);
		type = type_axe;
		name = "신범식의 도끼";
		down1 = setup("/objects/axe",gp.tileSize,gp.tileSize);
		attackValue = 2;
		attackArea.width = 30;
		attackArea.height = 30;
		description = "[" + name + "]\n좀 지저분하지만 아직 \n 나무를 자를수 있다.";
		price = 75;
	}

}
