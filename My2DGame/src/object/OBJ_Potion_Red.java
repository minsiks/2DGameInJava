package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity{

	GamePanel gp;
	
	public OBJ_Potion_Red(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = type_consumable;
		value = 5;
		name = "���� ����";
		down1 = setup("/objects/potion_red",gp.tileSize,gp.tileSize);
		description = "[" + name + "]\n+"+ value+ "ġ���� �ݴϴ�";
	}
	public void use(Entity entity) {
		
		gp.gameState = gp.dialogueState;
		gp.ui.currentDialogue = name + "�� ���̴�! \n"
				+ value + "��ŭ ü���� ȸ���ȴ�~!";
		entity.life += value;
		
		gp.playSE(2);
	}

}
