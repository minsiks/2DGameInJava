package entity;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class NPC_OldMan extends Entity{
	
	public NPC_OldMan(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 1;
		
		getImage();
		setDialogue();
	}
	public void getImage() {
		up1 = setup("/npc/oldman_up_1",gp.tileSize, gp.tileSize);
		up2 = setup("/npc/oldman_up_2",gp.tileSize, gp.tileSize);
		down1 = setup("/npc/oldman_down_1",gp.tileSize, gp.tileSize);
		down2 = setup("/npc/oldman_down_2",gp.tileSize, gp.tileSize);
		left1 = setup("/npc/oldman_left_1",gp.tileSize, gp.tileSize);
		left2 = setup("/npc/oldman_left_2",gp.tileSize, gp.tileSize);
		right1 = setup("/npc/oldman_right_1",gp.tileSize, gp.tileSize);
		right2 = setup("/npc/oldman_right_2",gp.tileSize, gp.tileSize);
	}
	public void setDialogue() {
		dialogues[0] = "네이름은 진경이가 맞나?";
		dialogues[1] = "아니면 타이거 JK..?";
		dialogues[2] = "암쏘 쏘리 벗 알러뷰 다 그짓말 ... \n비 투더 아 투더 뱅뱅 비 투..!\n난 권지용 이지..";
		dialogues[3] = "흠.. 일단 행운을 비네..!";
		
	}
	public void setAction() {
		
		actionLockCounter ++;
		
		if(actionLockCounter == 120) {
			Random random = new Random();
			int i = random.nextInt(100)+1; // pick up a number from 1 to 100
			
			if(i <= 25) {
				direction = "up";
			}
			if(i > 25 && i <= 50) {
				direction = "down";
			}
			if(i > 50 && i <=75) {
				direction = "left";
			}
			if(i > 75 && i <=100) {
				direction = "right";
			}
			actionLockCounter = 0;
		}
		
	}
	public void speak() {
		// Do this character specific stuff
		super.speak();
	}
}