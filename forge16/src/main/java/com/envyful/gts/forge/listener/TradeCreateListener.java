package com.envyful.gts.forge.listener;

import com.envyful.api.concurrency.UtilConcurrency;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.api.forge.listener.LazyListener;
import com.envyful.api.forge.player.ForgeEnvyPlayer;
import com.envyful.gts.api.Trade;
import com.envyful.gts.forge.EnvyGTSForge;
import com.envyful.gts.forge.event.TradeCreateEvent;
import com.envyful.gts.forge.impl.trade.type.PokemonTrade;
import com.envyful.gts.forge.player.GTSAttribute;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TradeCreateListener extends LazyListener {

    private final EnvyGTSForge mod;

    public TradeCreateListener(EnvyGTSForge mod) {
        super();

        this.mod = mod;
    }

    @SubscribeEvent
    public void onTradeCreate(TradeCreateEvent event) {
        if (!this.mod.getConfig().isEnableNewListingBroadcasts()) {
            return;
        }

        UtilConcurrency.runAsync(() -> {
            for (ForgeEnvyPlayer onlinePlayer : this.mod.getPlayerManager().getOnlinePlayers()) {
                GTSAttribute attribute = onlinePlayer.getAttribute(EnvyGTSForge.class);

                if (attribute == null || !attribute.getSettings().isToggledBroadcasts()) {
                    continue;
                }

                for (String s : this.mod.getLocale().getMessages().getCreateTradeBroadcast(this.getPokemon(event.getTrade()))) {
                    s = s.replace("%player%", event.getPlayer().getName())
                            .replace("%name%", event.getTrade().getDisplayName())
                            .replace("%cost%",
                                    String.format(EnvyGTSForge.getInstance().getLocale().getMoneyFormat(),
                                            event.getTrade().getCost()));

                    onlinePlayer.message(UtilChatColour.colour(s));
                }
            }
        });
    }

    private Pokemon getPokemon(Trade trade) {
        if (trade instanceof PokemonTrade) {
            return ((PokemonTrade) trade).getPokemon();
        }

        return null;
    }
}
