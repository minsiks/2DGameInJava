package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity{

	public OBJ_Sword_Normal(GamePanel gp) {
		super(gp);
		type = type_sword;
		name = "���Į";
		down1 = setup("/objects/sword_normal",gp.tileSize,gp.tileSize);
		attackValue = 1;
		description = "[" + name + "]\n ������ Į";
		
		attackArea.width = 36;
		attackArea.height = 36;
	}

}
