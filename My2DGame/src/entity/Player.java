package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity{

	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	int standCounter =0;
	public boolean attackCanceled = false;
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxIventorySize = 20;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		
		this.gp = gp;
		this.keyH = keyH;
		this.screenX = gp.screenWidth/2 - (gp.tileSize/2);
		this.screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
//		attackArea.width = 36;
//		attackArea.height = 36;
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttacImage();
		setItems();
	}
	public void setDefaultValues() {
		
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
//		worldX = gp.tileSize * 10;
//		worldY = gp.tileSize * 13;
		speed = 4;
		direction = "down";
		
		// PLAYER STATUS
		level = 1;
		maxLife = 6;
		life = maxLife;
		strength = 1; // The more strength he has, the more damage he gives.
		dexterity = 1; // The more dexterity he has, the less damage he receives.
		exp = 0;
		nextLevelExp = level*5;
		coin = 0;
		currentWeapon = new OBJ_Sword_Normal(gp);
		currentShield = new OBJ_Shield_Wood(gp);
		attack = getAttack(); // The total attack value is decided by strength and weapon
		defense  = getDefense();  // The total defense value is decided by dexterity
	}
	public void setItems() {
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new OBJ_Key(gp));
	}
	public int getAttack() {
		attackArea = currentWeapon.attackArea;
		return attack = strength * currentWeapon.attackValue;
	}
	public int getDefense() {
		return defense = dexterity * currentShield.defenseValue;
	}
	public void getPlayerImage() {
		up1 = setup("/player/boy_up_1",gp.tileSize, gp.tileSize);
		up2 = setup("/player/boy_up_2",gp.tileSize, gp.tileSize);
		down1 = setup("/player/boy_down_1",gp.tileSize, gp.tileSize);
		down2 = setup("/player/boy_down_2",gp.tileSize, gp.tileSize);
		left1 = setup("/player/boy_left_1",gp.tileSize, gp.tileSize);
		left2 = setup("/player/boy_left_2",gp.tileSize, gp.tileSize);
		right1 = setup("/player/boy_right_1",gp.tileSize, gp.tileSize);
		right2 = setup("/player/boy_right_2",gp.tileSize, gp.tileSize);
	}
	public void getPlayerAttacImage() {
		if(currentWeapon.type == type_sword) {
			attackUp1 = setup("/player/boy_attack_up_1",gp.tileSize, gp.tileSize*2);
			attackUp2 = setup("/player/boy_attack_up_2",gp.tileSize, gp.tileSize*2);
			attackDown1 = setup("/player/boy_attack_down_1",gp.tileSize, gp.tileSize*2);
			attackDown2 = setup("/player/boy_attack_down_2",gp.tileSize, gp.tileSize*2);
			attackLeft1 = setup("/player/boy_attack_left_1",gp.tileSize*2, gp.tileSize);
			attackLeft2 = setup("/player/boy_attack_left_2",gp.tileSize*2, gp.tileSize);
			attackRight1 = setup("/player/boy_attack_right_1",gp.tileSize*2, gp.tileSize);
			attackRight2 = setup("/player/boy_attack_right_2",gp.tileSize*2, gp.tileSize);
		}
		if(currentWeapon.type == type_axe) {
			attackUp1 = setup("/player/boy_axe_up_1",gp.tileSize, gp.tileSize*2);
			attackUp2 = setup("/player/boy_axe_up_2",gp.tileSize, gp.tileSize*2);
			attackDown1 = setup("/player/boy_axe_down_1",gp.tileSize, gp.tileSize*2);
			attackDown2 = setup("/player/boy_axe_down_2",gp.tileSize, gp.tileSize*2);
			attackLeft1 = setup("/player/boy_axe_left_1",gp.tileSize*2, gp.tileSize);
			attackLeft2 = setup("/player/boy_axe_left_2",gp.tileSize*2, gp.tileSize);
			attackRight1 = setup("/player/boy_axe_right_1",gp.tileSize*2, gp.tileSize);
			attackRight2 = setup("/player/boy_axe_right_2",gp.tileSize*2, gp.tileSize);
		}
	}
	public void update() {
		
		if(attacking == true) {
			attacking();
		} else if(keyH.upPressed == true || keyH.downPressed == true ||
				keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {
			
			if(keyH.upPressed == true) {
				direction = "up";
			}else if(keyH.downPressed == true) {
				direction = "down";
			}else if(keyH.leftPressed == true) {
				direction = "left";
			}else if(keyH.rightPressed == true) {
				direction = "right";
			}
			
			// CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			//CHECK OBJECT COLILISION
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			// CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			// CEHCK MONSTER COLLISION
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contackMonster(monsterIndex);
			
			// CHECK EVENT 
			gp.eHandler.checkEvent();
			
			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(collisionOn == false && keyH.enterPressed == false) {
				switch (direction) {
				case "up": worldY -= speed; break;
				case "down": worldY += speed; break;
				case "left": worldX -= speed; break;
				case "right": worldX += speed; break;
				}
			}
			
			if(keyH.enterPressed == true && attackCanceled == false) {
				gp.playSE(7);
				attacking = true;
				spriteCounter = 0;
			}
			
			attackCanceled = false;
			gp.keyH.enterPressed = false;
			
			spriteCounter++;
			if(spriteCounter > 12) {
				if(spriteNum ==1) {
					spriteNum =2;
				}else if(spriteNum == 2) {
					spriteNum =1;
				}
				spriteCounter = 0;
			}
		}
		else {
			standCounter++;
			if(standCounter ==20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}
		
		// This needs to be outside of key if statement!
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
	}
	private void attacking() {

		spriteCounter++;
		if(spriteCounter <= 5) {
			spriteNum =1;
		}
		if(spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
			
			// Save the current worldX, worldYm solidArea
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			// Adjust player's worldX/Y for the attackArea
			switch(direction) {
			case "up": worldY -= attackArea.height; break;
			case "down" : worldY += attackArea.height; break;
			case "left" : worldX -= attackArea.width; break;
			case "right" : worldX += attackArea.width; break;
			}
			// attackArea becomes solidArea
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			// Check monster collision with the updated worldX, worldY and solidArea
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex);
			
			// After checking collision, resort the original data
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
			
		}if(spriteCounter > 25){
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}
	public void pickUpObject(int i) {
		
		if(i != 999) {
			String text;
			if(inventory.size() != maxIventorySize) {
				inventory.add(gp.obj[i]);
				gp.playSE(1);
				text =  gp.obj[i].name + " 를 얻었다!";
			}else {
				text = "더 들수 없어요!";
			}
			gp.ui.addMessae(text);
			gp.obj[i]=null;
		}
	}
	public void interactNPC(int i ) {
		if(gp.keyH.enterPressed == true) {
			if(i != 999) {
				attackCanceled = true;
				gp.gameState = gp.dialogueState;
				gp.npc[i].speak();
			} 
		}
		
	}
	public void contackMonster(int i) {
		if(i != 999) {
			if(invincible == false) {
				gp.playSE(6);
				
				int damage = gp.monster[i].attack -defense;
				if(damage < 0) {
					damage = 0;
				}
				life -= damage;
				invincible = true;
			}
			
		}
	}
	private void damageMonster(int i) {
		if(i != 999) {
			if(gp.monster[i].invincible == false) {
				
				gp.playSE(5);
				
				int damage = attack - gp.monster[i].defense;
				if(damage<0) {
					damage =0;
				}
				
				gp.monster[i].life -= damage;
				gp.ui.addMessae(damage + " 데미지!");
				gp.monster[i].invincible = true;
				gp.monster[i].damageReaction();
				
				if(gp.monster[i].life <= 0) {
					gp.monster[i].dying = true;
					gp.ui.addMessae(gp.monster[i].name + "을 잡았습니다!");
					gp.ui.addMessae("경헙치 " + gp.monster[i].exp +" +");
					exp += gp.monster[i].exp;
					checkLevelUp();
				}
			}
		}
	}
	private void checkLevelUp() {
		if (exp >= nextLevelExp) {
			
			level++;
			nextLevelExp = nextLevelExp*2;
			maxLife += 2;
			strength++;
			dexterity++;
			attack = getAttack();
			defense = getDefense();
			
			gp.playSE(8);
			gp.gameState = gp.dialogueState;
			gp.ui.currentDialogue = "이제 " + level + " 레벨이 되었어요! \n" 
					+ "강해진것 같아유~!";
			
		}
	}
	public void selectItem() {
		int itemIndex = gp.ui.getItemIndexOnSlot();
		
		if(itemIndex< inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);
			
			if(selectedItem.type == type_sword || selectedItem.type == type_axe) {
				currentWeapon = selectedItem;
				attack = getAttack();
				getPlayerAttacImage();
			}
			if(selectedItem.type == type_shield) {
				currentShield = selectedItem;
				defense = getDefense();
			}
			if(selectedItem.type == type_consumable) {
				selectedItem.use(this);
				inventory.remove(itemIndex);
			}
		}
	}
	public void draw(Graphics2D g2) {
//		g2.setColor(Color.white);
		
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		
		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		switch (direction) {
		case "up":
			if(attacking == false) {
				if(spriteNum == 1) {image = up1;}
				if(spriteNum ==2) {image = up2;}
			}
			if(attacking == true) {
				tempScreenY = screenY - gp.tileSize;
				if(spriteNum == 1) {image = attackUp1;}
				if(spriteNum == 2) {image = attackUp2;}
			}
			break;
		case "down":
			if(attacking == false) {
				if(spriteNum == 1) {image = down1;}
				if(spriteNum ==2) {image = down2;}
			}
			if(attacking == true) {
				if(spriteNum == 1) {image = attackDown1;}
				if(spriteNum == 2) {image = attackDown2;}
			}
			break;
		case "left":
			if(attacking == false) {
				if(spriteNum == 1) {image = left1;}
				if(spriteNum ==2) {image = left2;}
			}
			if(attacking == true) {
				tempScreenX = screenX - gp.tileSize;
				if(spriteNum == 1) {image = attackLeft1;}
				if(spriteNum == 2) {image = attackLeft2;}
			}
			break;
		case "right":
			if(attacking == false) {
				if(spriteNum == 1) {image = right1;}
				if(spriteNum ==2) {image = right2;}
			}
			if(attacking == true) {
				if(spriteNum == 1) {image = attackRight1;}
				if(spriteNum == 2) {image = attackRight2;}
			}
			
			break;
		}
		if(invincible == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
		}
		
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		
		// Reset alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
		
		// DEBUG
//		g2.setFont(new Font("Arial", Font.PLAIN, 26));
//		g2.setColor(Color.white);
//		g2.drawString("Invincible:" + invincibleCounter , 10, 400);
	}
	
}
