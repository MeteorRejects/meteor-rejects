package anticope.rejects.modules;

import anticope.rejects.MeteorRejectsAddon;
import net.minecraft.util.math.Vec3d;

import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;

public class Boost extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> strength = sgGeneral.add(new DoubleSetting.Builder()
        .name("strength")
        .description("Strength to yeet you with.")
        .defaultValue(0.5)
        .min(0.1)
        .sliderMax(10)
        .build()
    );
    
    private final Setting<Boolean> autoBoost = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-boost")
        .description("Automatically boosts you.")
        .defaultValue(false)
        .build()
    );
    
    private final Setting<Integer> delaySetting = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("delay between each boost")
        .visible(autoBoost::get)
        .defaultValue(20)
        .range(0, 120)
        .sliderRange(0, 120)
        .build()
    );

    public Boost() {
        super(MeteorRejectsAddon.CATEGORY, "boost", "Works like a dash move.");
    }
    
    private int delay = 0;
    
    @Override
    public void onActivate() {
        boostPlayer();
        delay = delaySetting.get();
        if(!autoBoost.get()) this.toggle();
    }
    
    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if(autoBoost.get()) {
            if(delay <= 1) {
                boostPlayer();
                delay = delay.get();
            } else {
                delay--;
            }
        }
    }
    
    private void boostPlayer() {
        if (mc.player ==  null) return;
        Vec3d v = mc.player.getRotationVecClient().multiply(strength.get());
        mc.player.addVelocity(v.getX(), v.getY(), v.getZ());   
    }
}
