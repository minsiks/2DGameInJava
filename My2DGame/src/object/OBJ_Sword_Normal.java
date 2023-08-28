package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity{

	public OBJ_Sword_Normal(GamePanel gp) {
		super(gp);
		type = type_sword;
		name = "기냥칼";
		down1 = setup("/objects/sword_normal",gp.tileSize,gp.tileSize);
		attackValue = 1;
		description = "[" + name + "]\n 기냥칼이다.";
		
		attackArea.width = 36;
		attackArea.height = 36;
		price = 20;
	}

}
