# AutoPlay-1.0
 A forge mod (link to Readme.md)
Minecraft: 1.20.2, Forge: 48.1.0, Dependencies: Geckolib-1.20.2-4.3.1
zh_cn:
大致主线：
    1.收集铁做铁手臂和铁假肢并右键融合，以及对应的数据库（如：铁手臂控制数据库）
    1-1(选做).用铁和蓝色染料做U盘，副手持数据库，主手持U盘，右键可以查询对应工具的等级（如：副手铁手臂控制数据库， 主手U盘：铁手臂等级：1阶 14级 [换行] 品质：传奇）
    1-2-1.品质作用：铁手臂普通：每一阶可增加0.2点攻击伤害，满阶+2.0，铁手臂稀有：每一阶+0.3，铁手臂史诗：每一阶+0.4，铁手臂传奇：每一阶+0.5
    1-2-2.铁假肢普通：每一阶可增加3%击退减免，满阶+30%，铁假肢稀有：每一阶+4%，铁假肢史诗：每一阶+6%，铁假肢传奇：每一阶+8%
    1-3.融合时有70%普通，20%稀有，8%史诗，2%传奇
    1-4.等级作用：见3
    2.获取数据之瓶，副手持数据库，主手持数据之瓶右键可升级，最大100级
    2-1.合成空数据之瓶后，右键，会扣除100点经验值，并获得一个数据之瓶
    3.25级可放1技能，50级2技能，75级3技能，100级可升阶，升阶会清空等级，等阶+1（机器人控制数据库除外，见7和8）
    3-1.副手持数据库，按下上下左箭头可放123技能，1技能消耗1点能量，2技能消耗3点，3技能消耗5点，吃红枣牌充电宝可回满能量成10点，能量UI在饱食度上方。右箭头升阶，清空能量。
    3-2-1.铁手臂技能：1技能：对10*4*10范围内一个生物造成伤害，伤害点数为玩家当前的伤害点数，无攻击间隔（玩家除外），2技能：对6*6*6范围内的所有实体造成伤害（玩家除外），3技能往后撤一段距离（就是当前视角的反方向），然后喷出粒子光束，破坏光束内的方块，对光束内的实体造成50点伤害。
    3-2-2.铁假肢技能：1技能：无论何时，跳一下，在空中，在水里，都能跳，2技能：向前冲刺，无论何地，3技能，身后会爆炸，玩家会朝目光方向喷射出去。
    4.合成润滑油提纯器，放入64桶水、岩浆、牛奶可合成一桶润滑油。
    5.合成chatgpt数据库放在地上，每个随机刻有1/31的概率生成一个机器人，1000hp。机器人有3个技能：tnt炮，召唤铁傀儡，恐吓。将机器人打到≤50hp后对着机器人下方喷虚弱药水，用润滑油右键机器人可融合机器人，机器人会消失。融合后可升到3阶，否则最大2阶
    6.主世界的沙漠会生成实验室，其中箱子有概率生成机器人数据，9个机器人数据合成完整机器人数据，可合成机器人控制数据库。
    7.机器人控制数据库只有2个技能，与手臂假肢不一样的是它不能升阶也不能升级。
    7-1.机器人控制数据库技能：1技能，对一个方块（5格内）使用，可找到99*99*99范围内的11个方块并挖掘，2技能：对一个方块（49格内）使用，可走到指定地点
    8.融合后机器人的2个技能对等级没有要求，但是要求手臂假肢的等阶均≥6
    9.每释放一个机器人控制数据库的技能，熟练度+1，当熟练度≥100时，可以升至7阶，否则最大6阶
    10.当手臂假肢均达到10阶时，玩家可以右键末地坐标0 50 0的封印方块，龙变得可被伤害，血量为200点
    11.杀完龙跳进传送门算通关
注意事项：
    1.铁手臂3技能会造成很多掉落物，且很吃性能，用几次之后就会很卡，少用
    2.铁假肢3技能不要一直用，速度会叠加，我在测试的时候对着天一边吃充电宝一边用三技能，飞到了Y轴48亿多米的高空
    3.机器人的两个技能可能会有一点小问题，但问题不大，会自己修复（如果真的有问题就退出重进）
    4.遇到任何问题（像什么无法充电之类的）退出重进
en_us: (translated by Google)
Main line:
1. Collect iron to make iron arms and iron prostheses and fuse them with right click, as well as the corresponding database (such as: iron arm control database)
1-1 (optional). Use iron and blue dye to make a USB flash drive, hold the database in the off-hand hand, and the USB flash drive in the main hand. Right click to query the level of the corresponding tool (such as: off-hand iron arm control database, main hand USB flash drive: Iron arm level: level 14 [line break] quality: legendary)
1-2-1. Quality effect: Iron arm ordinary: each level can increase 0.2 points of attack damage, full level +2.0, iron arm rare: each level +0.3, iron arm epic: each level +0.4, iron arm legendary: each level +0.5
1-2-2. Iron prosthesis ordinary: each level can increase 3% knockback reduction, full level +30%, iron prosthesis rare: each level +4%, iron prosthesis epic: each level +6%, iron prosthesis legendary: each level +8%
1-3. When fused, there are 70% common, 20% rare, 8% epic, and 2% legendary
1-4. Level effect: see 3
2. Get the data bottle, hold the database in the secondary hand, and right-click the data bottle in the primary hand to upgrade, the maximum level is 100
2-1. After synthesizing an empty data bottle, right-click, 100 experience points will be deducted, and a data bottle will be obtained
3. Level 25 can release skill 1, level 50 skill 2, level 75 skill 3, level 100 can be upgraded, upgrading will clear the level, level +1 (except for the robot control database, see 7 and 8)
3-1. Hold the database in the secondary hand, press the up, down, left and right arrows to release skills 123, skill 1 consumes 1 energy, skill 2 consumes 3 energy, and skill 3 consumes 5 energy. Eating the red date brand power bank can restore the energy to 10 points. The energy UI is above the satiety. Right arrow upgrades and clears energy.
3-2-1. Iron Arm Skills: Skill 1: Causes damage to a creature within a 10*4*10 range, the damage points are the player's current damage points, and there is no attack interval (except the player). Skill 2: Causes damage to all entities within a 6*6*6 range (except the player). Skill 3: Retreats a distance (the opposite direction of the current perspective), then sprays out a particle beam to destroy the blocks in the beam, causing 50 points of damage to the entity in the beam.
3-2-2. Iron Prosthetic Skills: Skill 1: Jump at any time, in the air, in the water, you can jump, Skill 2: Dash forward, no matter where, Skill 3: There will be an explosion behind you, and the player will spray in the direction of the gaze.
4. Synthesize lubricant purifier, put in 64 barrels of water, magma, and milk to synthesize a barrel of lubricant.
5. Synthesize chatgpt database and place it on the ground. Each random engraving has a 1/31 probability of generating a robot, 1000hp. The robot has 3 skills: TNT cannon, summon iron golem, and intimidate. After the robot is beaten to ≤50hp, spray the Weakness Potion on the bottom of the robot. Use lubricant to right-click the robot to fuse the robot, and the robot will disappear. After fusion, it can be upgraded to level 3, otherwise the maximum level is 2. 6. A laboratory will be generated in the desert of the main world, in which the box has a chance to generate robot data. 9 robot data are synthesized into complete robot data, which can be synthesized into a robot control database. 7. The robot control database has only 2 skills. Unlike the arm prosthesis, it cannot be upgraded or upgraded. 7-1. Robot control database skills: Skill 1, used on a block (within 5 grids), can find 11 blocks within the range of 99*99*99 and dig, Skill 2: used on a block (within 49 grids), can walk to the designated location
8. After fusion, the two skills of the robot have no level requirements, but the level of the arm prosthesis is required to be ≥6
9. Each time a skill in the robot control database is released, the proficiency is +1. When the proficiency is ≥100, it can be upgraded to level 7, otherwise the maximum level is 6
10. When the arm prosthesis reaches level 10, the player can right-click the seal block at the end coordinates 0 50 0, and the dragon becomes vulnerable, with a health of 200 points
11. After killing the dragon, jump into the portal to pass the level
Notes:
1. Iron Arm 3 skill will cause a lot of drops and is very performance-intensive. It will be very stuck after using it a few times, so use it less
2. Don't use the 3rd skill of the iron prosthesis all the time, the speed will be superimposed. When I was testing, I used the 3rd skill while eating the power bank and flew to an altitude of more than 4.8 billion meters on the Y axis.
3. The robot's two skills may have some minor problems, but the problem is not big, and they will be repaired by themselves (if there is a problem, exit and re-enter)
4. If you encounter any problems (such as being unable to charge, etc.), exit and re-enter
