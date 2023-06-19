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
		name = "빨간 물약";
		down1 = setup("/objects/potion_red",gp.tileSize,gp.tileSize);
		description = "[" + name + "]\n+"+ value+ "치유해 줍니다";
	}
	public void use(Entity entity) {
		
		gp.gameState = gp.dialogueState;
		gp.ui.currentDialogue = name + "을 마셨다! \n"
				+ value + "만큼 체력이 회복된다~!";
		entity.life += value;
		
		gp.playSE(2);
	}

}
