/*
 * Java classes obtained by decompiling the jar file form a Raspberry Pi
 * And adapted by d bowes
 */
package minecraft;

import pi.Block;
import pi.Minecraft;
import pi.Vec;

/**
 *
 * @author d bowes
 */
public class Minecrafter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        Block b = Block.SANDSTONE;
        int m = 1;
        Block t = Block.TORCH;
        String host="csse-minecraft[1|2|3].canterbury.ac.nz";
        String token="as provided by email";
        if (args.length>0){
            host=args[0];
        }
        if (args.length>1){
            token=args[1];
        }
        Minecraft mc = Minecraft.connect(host,token);

        Vec pos = mc.player.getPosition();
        Minecraft.Entities e = mc.entities;
        int[] ids = mc.getPlayerEntityIds();
        for (int i : ids) {
            System.out.println("id: " + i);
            System.out.println("\t" + e.getPosition(i));
        }
        
        Vec pp = Vec.xyz(pos.x + 1, pos.y, pos.z);
        e.setPosition(3, pp);

        mc.setBlock(pos, Block.WOOL);
        for (int n = 0; n < m; n++) {
            for (int height = -1; height < 4; height++) {

                mc.setBlocks(pos.x - 4, pos.y + height, pos.z - 4,
                        pos.x + 4, pos.y + height, pos.z - 4, b);

                mc.setBlocks(pos.x - 4, pos.y + height, pos.z + 4,
                        pos.x + 4, pos.y + height, pos.z + 4, b);
                mc.setBlocks(pos.x + 4, pos.y + height, pos.z + 4,
                        pos.x + 4, pos.y + height, pos.z - 4, b);
                mc.setBlocks(pos.x - 4, pos.y + height, pos.z - 4,
                        pos.x - 4, pos.y + height, pos.z + 4, b);

            }
            int height = 4;
            mc.setBlocks(pos.x - 4, pos.y + height, pos.z - 4,
                    pos.x + 4, pos.y + height, pos.z + 4, b);
            height = -1;
            mc.setBlocks(pos.x - 4, pos.y + height, pos.z - 4,
                    pos.x + 4, pos.y + height, pos.z + 4, b);

            for (height = 0; height < 3; height++) {
                for (int x = -2; x < 3; x++) {

                    mc.setBlock(pos.x + 3, pos.y + height, pos.z + x, Block.AIR);
                    mc.setBlock(pos.x - 3, pos.y + height, pos.z + x, Block.AIR);
                    mc.setBlock(pos.x + x, pos.y + height, pos.z - 3, Block.AIR);
                    mc.setBlock(pos.x + x, pos.y + height, pos.z + 3, Block.AIR);

                    mc.setBlock(pos.x + 3, pos.y + height, pos.z + x, t);
                    mc.setBlock(pos.x - 3, pos.y + height, pos.z + x, t);
                    mc.setBlock(pos.x + x, pos.y + height, pos.z - 3, t);
                    mc.setBlock(pos.x + x, pos.y + height, pos.z + 3, t);
                }
            }

            mc.flush();
        }
        mc.postToChat("Fire!");
        boolean ok = true;
        int time=0;
        while (ok) {
            Thread.sleep(500L);
            System.out.println(mc.getChats());            
            pos = mc.player.getPosition();
            int height = 2;
            int w = 2;
            mc.setBlocks(pos.x - w, pos.y - w, pos.z - w,
                    pos.x + w, pos.y + w, pos.z + w, Block.AIR);
            //mc.setBlock(pos.x, pos.y - 2, pos.z, Block.BEDROCK);
            mc.setBlock(pos.x-w, pos.y - w, pos.z-w, Block.TORCH);
            mc.setBlock(pos.x+w, pos.y + w, pos.z+w, Block.TORCH);
            mc.setBlock(pos.x-w, pos.y - w, pos.z-w, Block.TORCH);
            mc.setBlock(pos.x+w, pos.y - w, pos.z+w, Block.TORCH);
            mc.setBlock(pos.x-w, pos.y - w, pos.z-w, Block.TORCH);
            mc.setBlock(pos.x+w, pos.y + w, pos.z-w, Block.TORCH);
            mc.setBlock(pos.x-w, pos.y - w, pos.z-w, Block.TORCH);
            mc.setBlock(pos.x-w, pos.y + w, pos.z+w, Block.TORCH);
            
        }

    }

}
