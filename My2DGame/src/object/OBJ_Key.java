package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity{
	
	public OBJ_Key(GamePanel gp) {

		super(gp);
		
		name = "����";
		down1 = setup("/objects/key",gp.tileSize, gp.tileSize);
		description = "[" + name + "]\n ���� ���� �ִ�.";
	}
}
