package net.ilexiconn.lawnchair;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import net.ilexiconn.lawnchair.api.LawnchairAPI;
import net.ilexiconn.lawnchair.server.ServerProxy;
import net.ilexiconn.lawnchair.server.block.FabricatorBlock;
import net.ilexiconn.lawnchair.server.block.FishBowlBlock;
import net.ilexiconn.lawnchair.server.block.entity.FabricatorEntity;
import net.ilexiconn.lawnchair.server.impl.LawnchairAPIImpl;
import net.ilexiconn.lawnchair.server.item.BlockItem;
import net.ilexiconn.lawnchair.server.network.ClearFabricatorMessage;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "lawnchair", name = "Lawnchair", version = Lawnchair.VERSION, dependencies = "required-after:llibrary@[" + Lawnchair.LLIBRARY_VERSION + ",)")
public class Lawnchair {
    public static final String VERSION = "1.0.0";
    public static final String LLIBRARY_VERSION = "1.5.1";

    @Mod.Instance("lawnchair")
    public static Lawnchair INSTANCE;
    @SidedProxy(serverSide = "net.ilexiconn.lawnchair.server.ServerProxy", clientSide = "net.ilexiconn.lawnchair.client.ClientProxy")
    public static ServerProxy PROXY;
    @NetworkWrapper({ClearFabricatorMessage.class})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    public static Logger LOGGER = LogManager.getLogger("Lawnchair");

    public static final Block FABRICATOR = new FabricatorBlock();
    public static final Item FABRICATOR_ITEM = new BlockItem(Lawnchair.FABRICATOR);
    public static final Block FISH_BOWL = new FishBowlBlock();
    public static final Item FISH_BOWL_ITEM = new BlockItem(Lawnchair.FISH_BOWL);

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        Lawnchair.PROXY.onPreInit();

        GameRegistry.register(Lawnchair.FABRICATOR);
        GameRegistry.register(Lawnchair.FABRICATOR_ITEM);
        GameRegistry.registerTileEntity(FabricatorEntity.class, "Fabricator");
        GameRegistry.register(Lawnchair.FISH_BOWL);
        GameRegistry.register(Lawnchair.FISH_BOWL_ITEM);

        LawnchairAPIImpl.INSTANCE.registerFabricatorRecipe(Lawnchair.FISH_BOWL, new ItemStack(Items.FISH, 2, ItemFishFood.FishType.CLOWNFISH.getMetadata()), new ItemStack(Blocks.GLASS_PANE, 5));
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        Lawnchair.PROXY.onInit();
    }

    @Mod.EventHandler
    public void onIMC(FMLInterModComms.IMCEvent event) {
        event.getMessages().stream().filter(message -> message.key.equalsIgnoreCase("api")).forEach(message -> {
            Optional<Function<LawnchairAPI, Void>> value = message.getFunctionValue(LawnchairAPI.class, Void.class);
            if (value.isPresent()) {
                value.get().apply(LawnchairAPIImpl.INSTANCE);
            }
        });
    }
}
