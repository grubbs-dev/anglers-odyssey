package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.fish.FishRarity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.List;

/** Compact 26.1 extraction-based journal screen. */
public class FishJournalScreen extends Screen {
    private final int totalFish, discovered, completion, shiny, totalCatches, trophies, rodTier;
    private final List<FishEntry> entries;
    private int page;
    private int scrollOffset;

    public FishJournalScreen(int totalFish, int discovered, int completion, int shiny, int totalCatches,
                             int trophies, int rodTier, List<FishEntry> entries) {
        super(Component.literal("Angler's Journal"));
        this.totalFish = totalFish; this.discovered = discovered; this.completion = completion;
        this.shiny = shiny; this.totalCatches = totalCatches; this.trophies = trophies; this.rodTier = rodTier;
        this.entries = entries;
    }

    @Override protected void init() {
        int y = Math.min(this.height - 28, 226);
        addRenderableWidget(Button.builder(Component.literal("< Previous"), b -> { if (page > 0) { page--; scrollOffset = 0; } })
                .bounds(this.width / 2 - 115, y, 105, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Next >"), b -> { if (page < 9) { page++; scrollOffset = 0; } })
                .bounds(this.width / 2 + 10, y, 105, 20).build());
    }

    @Override public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        // Screen extraction renders the inherited background/widgets in an earlier
        // stratum. Keep the journal panel and text above that layer on 26.x.
        g.nextStratum();
        int x = this.width / 2 - 160, y = 12, h = Math.min(210, this.height - 58);
        g.fill(x, y, x + 320, y + h, 0xDD111111); g.outline(x, y, 320, h, 0xFF3EF9C3);
        g.centeredText(font, pageTitle(), this.width / 2, 30, pageColorCode());
        if (page == 0) {
            line(g, "Species Found: " + discovered + " / " + totalFish, x + 22, 50, ChatFormatting.GREEN);
            line(g, "Completion: " + completion + "%", x + 22, 66, ChatFormatting.YELLOW);
            line(g, "Total Fish Caught: " + totalCatches, x + 22, 82, ChatFormatting.GOLD);
            line(g, "Shiny Species: " + shiny, x + 22, 98, ChatFormatting.AQUA);
            line(g, "Trophy Catches: " + trophies, x + 22, 114, ChatFormatting.LIGHT_PURPLE);
            line(g, "Rod tier: " + (rodTier == 0 ? "Vanilla Rod" : rodTier), x + 22, 140, ChatFormatting.GREEN);
        } else if (page <= 5) {
            FishRarity rarity = FishRarity.values()[page - 1];
            List<FishEntry> filtered = entries.stream().filter(e -> e.rarity() == rarity).toList();
            int end = Math.min(filtered.size(), scrollOffset + 10);
            for (int i = scrollOffset; i < end; i++) {
                FishEntry e = filtered.get(i);
                line(g, (e.discovered() ? e.displayName() : "???") + " — " + e.biome(), x + 22,
                        50 + (i - scrollOffset) * 14, e.discovered() ? rarity.getColor() : ChatFormatting.DARK_GRAY);
            }
        } else if (page == 6) {
            line(g, "Personal records", x + 22, 50, ChatFormatting.GOLD);
            int row = 0; for (FishEntry e : entries) if (e.recordWeight() > 0 && row < 10)
                line(g, e.displayName() + " — " + e.recordWeight() + " lbs, " + e.recordLength() + " in", x + 22, 66 + row++ * 14, ChatFormatting.YELLOW);
        } else if (page == 7) {
            line(g, "Shiny collection", x + 22, 50, ChatFormatting.AQUA);
            int row = 0; for (FishEntry e : entries) if (e.shiny() && row < 10)
                line(g, "✦ " + e.displayName() + " — " + e.biome(), x + 22, 66 + row++ * 14, ChatFormatting.AQUA);
        } else if (page == 8) {
            line(g, "Total catches: " + totalCatches, x + 22, 50, ChatFormatting.GOLD);
            line(g, "Species discovered: " + discovered, x + 22, 66, ChatFormatting.GREEN);
            line(g, "Trophies: " + trophies, x + 22, 82, ChatFormatting.LIGHT_PURPLE);
        } else {
            line(g, "Discover fish to upgrade your rod", x + 22, 50, ChatFormatting.GREEN);
            line(g, "Current tier: " + (rodTier == 0 ? "Vanilla Rod" : rodTier), x + 22, 68, ChatFormatting.AQUA);
        }
        g.centeredText(font, "Page " + (page + 1) + " / 10", this.width / 2, Math.min(this.height - 8, 252), 0xAAAAAA);
    }

    private void line(GuiGraphicsExtractor g, String text, int x, int y, ChatFormatting color) {
        g.text(font, Component.literal(text).withStyle(color), x, y, 0xFFFFFF);
    }

    private Component pageTitle() { return Component.literal(switch (page) {
        case 1 -> "Common Fish"; case 2 -> "Uncommon Fish"; case 3 -> "Rare Fish"; case 4 -> "Epic Fish";
        case 5 -> "Legendary Fish"; case 6 -> "Personal Records"; case 7 -> "Shiny Collection";
        case 8 -> "Fishing Stats"; case 9 -> "Rod Progression"; default -> "Angler's Journal";
    }); }
    private ChatFormatting pageColor() { return page == 7 ? ChatFormatting.AQUA : ChatFormatting.GOLD; }
    private int pageColorCode() { return page == 7 ? 0x55FFFF : 0xFFAA00; }
    @Override public boolean mouseClicked(MouseButtonEvent click, boolean doubleClick) { return super.mouseClicked(click, doubleClick); }
    @Override public boolean mouseScrolled(double x, double y, double horizontal, double vertical) {
        if (page >= 1 && page <= 5) scrollOffset = Math.max(0, scrollOffset - (int) Math.signum(vertical));
        return true;
    }
    @Override public boolean keyPressed(KeyEvent input) { return super.keyPressed(input); }

    public record FishEntry(Identifier id, String displayName, FishRarity rarity, String biome,
                            boolean discovered, boolean shiny, double recordWeight, int recordLength, int catchCount) {}
}
