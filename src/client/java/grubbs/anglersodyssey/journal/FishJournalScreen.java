package grubbs.anglersodyssey.journal;

import grubbs.anglersodyssey.fish.FishRarity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import java.util.Comparator;
import java.util.List;

public class FishJournalScreen extends Screen {

    private final int totalFish;
    private final int discovered;
    private final int completion;
    private final int shiny;
    private final int totalCatches;
    private final int trophies;
    private final int rodTier;
    private final List<FishEntry> entries;

    private int page = 0;
    private int scrollOffset = 0;
    private FishEntry selectedFish = null;
    private boolean viewingFishDetails = false;

    @Override
public boolean mouseClicked(double mouseX, double mouseY, int button) {

    if (page >= 1 && page <= 5 && !viewingFishDetails) {

        FishRarity rarity = switch (page) {
            case 1 -> FishRarity.COMMON;
            case 2 -> FishRarity.UNCOMMON;
            case 3 -> FishRarity.RARE;
            case 4 -> FishRarity.EPIC;
            case 5 -> FishRarity.LEGENDARY;
            default -> FishRarity.COMMON;
        };

        List<FishEntry> filtered = entries.stream()
                .filter(e -> e.rarity() == rarity)
                .toList();

                int panelX = this.width / 2 - 160;
                int panelY = 12;

                int x = panelX + 22;
                int y = panelY + 38;

        int rowHeight = 14;
        int maxRows = 10;

        int end = Math.min(filtered.size(), scrollOffset + maxRows);

        for (int i = scrollOffset; i < end; i++) {

            int rowY = y + ((i - scrollOffset) * rowHeight);

                if (mouseX >= x &&
                    mouseX <= x + 240 &&
                    mouseY >= rowY &&
                    mouseY <= rowY + rowHeight + 2) {

                FishEntry entry = filtered.get(i);

                if (entry.discovered()) {
                    selectedFish = entry;
                    viewingFishDetails = true;
                }

                return true;
            }
        }
    }

    return super.mouseClicked(mouseX, mouseY, button);
}

    public FishJournalScreen(
            int totalFish,
            int discovered,
            int completion,
            int shiny,
            int totalCatches,
            int trophies,
            int rodTier,
            List<FishEntry> entries
    ) {
        super(Text.literal("Angler's Journal"));
        this.totalFish = totalFish;
        this.discovered = discovered;
        this.completion = completion;
        this.shiny = shiny;
        this.totalCatches = totalCatches;
        this.trophies = trophies;
        this.rodTier = rodTier;
        this.entries = entries;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int bottomY = Math.min(this.height - 30, 226);

            this.addDrawableChild(ButtonWidget.builder(Text.literal("< Previous"), button -> {
                if (page > 0) {
                    page--;
                    scrollOffset = 0;
                }
            }).dimensions(centerX - 115, bottomY, 105, 20).build());

            this.addDrawableChild(ButtonWidget.builder(Text.literal("Next >"), button -> {
                if (page < getMaxPage()) {
                    page++;
                    scrollOffset = 0;
                }
            }).dimensions(centerX + 10, bottomY, 105, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Screen.render applies Minecraft's background blur and renders the
        // child buttons. It must run before the journal contents; calling it
        // at the end blurs the panel and all of its text on 1.21.1.
        super.render(context, mouseX, mouseY, delta);

        int panelX = this.width / 2 - 160;
        int panelY = 12;
        int panelW = 320;
        int panelH = Math.min(210, this.height - 58);

        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xDD111111);
        context.drawBorder(panelX, panelY, panelW, panelH, 0xFF3EF9C3);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal(getPageTitle().getString())
                        .formatted(getPageTitleColor(), Formatting.BOLD),
                this.width / 2,
                30,
                0xFFFFFF
        );

        int x = panelX + 22;
        int y = panelY + 38;

        if (viewingFishDetails && selectedFish != null) {
            renderFishDetails(context, x, y);
            return;
        }

        switch (page) {
            case 0 -> renderSummaryPage(context, x, y);
            case 1 -> renderRarityPage(context, x, y, FishRarity.COMMON, mouseX, mouseY);
            case 2 -> renderRarityPage(context, x, y, FishRarity.UNCOMMON, mouseX, mouseY);
            case 3 -> renderRarityPage(context, x, y, FishRarity.RARE, mouseX, mouseY);
            case 4 -> renderRarityPage(context, x, y, FishRarity.EPIC, mouseX, mouseY);
            case 5 -> renderRarityPage(context, x, y, FishRarity.LEGENDARY, mouseX, mouseY);
            case 6 -> renderRecordsPage(context, x, y);
            case 7 -> renderShinyPage(context, x, y);
            case 8 -> renderStatsPage(context, x, y);
            case 9 -> renderRodProgressionPage(context, x, y);
        }

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Page " + (page + 1) + " / " + (getMaxPage() + 1)).formatted(Formatting.GRAY),
                this.width / 2,
                Math.min(this.height - 8, 252),
                0xFFFFFF
        );

    }

        private Text getPageTitle() {
            return switch (page) {
                case 0 -> Text.literal("Angler's Journal");
                case 1 -> Text.literal("Common Fish");
                case 2 -> Text.literal("Uncommon Fish");
                case 3 -> Text.literal("Rare Fish");
                case 4 -> Text.literal("Epic Fish");
                case 5 -> Text.literal("Legendary Fish");
                case 6 -> Text.literal("Personal Records");
                case 7 -> Text.literal("Shiny Collection");
                case 8 -> Text.literal("Fishing Stats");
                case 9 -> Text.literal("Rod Progression");
                default -> Text.literal("Angler's Journal");
            };
        }

        private Formatting getPageTitleColor() {
    return switch (page) {
        case 1 -> FishRarity.COMMON.getColor();
        case 2 -> FishRarity.UNCOMMON.getColor();
        case 3 -> FishRarity.RARE.getColor();
        case 4 -> FishRarity.EPIC.getColor();
        case 5 -> FishRarity.LEGENDARY.getColor();

        case 6 -> Formatting.GOLD;          // Records
        case 7 -> Formatting.AQUA;          // Shinies
        case 8 -> Formatting.YELLOW;        // Stats
        case 9 -> Formatting.GREEN;         // Rods

        default -> Formatting.AQUA;         // Journal Summary
    };
}

private void renderSummaryPage(DrawContext context, int x, int y) {
    drawLine(context, "Species Found: " + discovered + " / " + totalFish, x, y, Formatting.GREEN);
    drawLine(context, "Completion: " + completion + "%", x, y + 14, Formatting.YELLOW);
    drawLine(context, "Total Fish Caught: " + totalCatches, x, y + 28, Formatting.GOLD);
    drawLine(context, "Shiny Species: " + shiny, x, y + 42, Formatting.AQUA);
    drawLine(context, "Trophy Catches: " + trophies, x, y + 56, Formatting.LIGHT_PURPLE);

    drawLine(context, "Collection by Rarity", x, y + 80, Formatting.GRAY);
    drawRarityProgress(context, x, y + 94, FishRarity.COMMON, "Common");
    drawRarityProgress(context, x, y + 108, FishRarity.UNCOMMON, "Uncommon");
    drawRarityProgress(context, x, y + 122, FishRarity.RARE, "Rare");
    drawRarityProgress(context, x, y + 136, FishRarity.EPIC, "Epic");
    drawRarityProgress(context, x, y + 150, FishRarity.LEGENDARY, "Legendary");
}
private void renderFishDetails(DrawContext context, int x, int y) {
    int leftX = x;
    int rightX = x + 155;

Item fishItem = Registries.ITEM.get(selectedFish.id());
ItemStack fishStack = new ItemStack(fishItem);

context.drawItem(fishStack, rightX + 34, y);

    drawLine(context, selectedFish.displayName(), leftX, y, selectedFish.rarity().getColor());

    drawLine(context, "Rarity:", leftX, y + 24, Formatting.GRAY);
    drawLine(context, selectedFish.rarity().getDisplayName(), leftX + 70, y + 24, selectedFish.rarity().getColor());

    drawLine(context, "Biome:", leftX, y + 40, Formatting.GRAY);
    drawLine(context, selectedFish.biome(), leftX + 70, y + 40, Formatting.DARK_AQUA);

    drawLine(context, "Personal Record", leftX, y + 72, Formatting.GOLD);
    drawLine(context, "Weight: " + selectedFish.recordWeight() + " lbs", leftX, y + 88, Formatting.YELLOW);
    drawLine(context, "Length: " + selectedFish.recordLength() + " in", leftX, y + 104, Formatting.YELLOW);

    drawLine(context, "Status", rightX, y + 36, Formatting.AQUA);

    drawLine(
            context,
            selectedFish.discovered() ? "✓ Discovered" : "✗ Undiscovered",
            rightX,
            y + 56,
            selectedFish.discovered() ? Formatting.GREEN : Formatting.RED
    );

    drawLine(
            context,
            selectedFish.shiny() ? "✓ Shiny Found" : "✗ No Shiny Found",
            rightX,
            y + 72,
            selectedFish.shiny() ? Formatting.AQUA : Formatting.GRAY
    );

    drawLine(context, "Total Caught", rightX, y + 104, Formatting.GOLD);
    drawLine(context, String.valueOf(selectedFish.catchCount()), rightX, y + 120, Formatting.YELLOW);

    drawLine(context, "ESC: Back", leftX, y + 150, Formatting.GRAY);
}
    private void drawRarityProgress(DrawContext context, int x, int y, FishRarity rarity, String label) {
        long total = entries.stream().filter(e -> e.rarity() == rarity).count();
        long found = entries.stream().filter(e -> e.rarity() == rarity && e.discovered()).count();

        context.drawTextWithShadow(
                this.textRenderer,
                Text.literal(label + ": " + found + " / " + total).formatted(rarity.getColor()),
                x,
                y,
                0xFFFFFF
        );
    }

private void renderRarityPage(DrawContext context, int x, int y, FishRarity rarity, int mouseX, int mouseY) {
    List<FishEntry> filtered = entries.stream()
            .filter(entry -> entry.rarity() == rarity)
            .toList();

    int rowHeight = 14;
    int maxVisibleRows = 10;
    int maxScroll = Math.max(0, filtered.size() - maxVisibleRows);

    if (scrollOffset > maxScroll) {
        scrollOffset = maxScroll;
    }

    int end = Math.min(filtered.size(), scrollOffset + maxVisibleRows);
    int lineY = y;

    for (int i = scrollOffset; i < end; i++) {
        FishEntry entry = filtered.get(i);

        Text line;

        if (entry.discovered()) {
            String prefix = entry.shiny() ? "✦ " : "✓ ";

            line = Text.literal(prefix)
                    .formatted(entry.shiny() ? Formatting.AQUA : Formatting.GREEN)
                    .append(Text.literal(entry.displayName()).formatted(entry.rarity().getColor()))
                    .append(Text.literal(" - " + entry.biome()).formatted(Formatting.DARK_AQUA));
        } else {
            line = Text.literal("✗ Undiscovered - " + entry.biome()).formatted(Formatting.DARK_GRAY);
        }

        boolean hovered = entry.discovered()
                && mouseX >= x
                && mouseX <= x + 240
                && mouseY >= lineY
                && mouseY <= lineY + rowHeight + 2;

        if (hovered) {
            context.fill(x - 4, lineY - 2, x + 250, lineY + rowHeight, 0x663EF9C3);
        }

        context.drawTextWithShadow(this.textRenderer, line, x, lineY, 0xFFFFFF);
        lineY += rowHeight;
    }

    if (filtered.size() > maxVisibleRows) {
        drawLine(
                context,
                "Scroll: " + (scrollOffset + 1) + "-" + end + " / " + filtered.size(),
                x,
                y + (maxVisibleRows * rowHeight) + 8,
                Formatting.GRAY
        );
    }
}

    private void renderRecordsPage(DrawContext context, int x, int y) {
        List<FishEntry> records = entries.stream()
                .filter(FishEntry::discovered)
                .filter(entry -> entry.recordWeight() > 0)
                .sorted(Comparator.comparingDouble(FishEntry::recordWeight).reversed())
                .limit(10)
                .toList();

        if (records.isEmpty()) {
            drawLine(context, "No records yet.", x, y, Formatting.GRAY);
            return;
        }

        int lineY = y;

        for (FishEntry entry : records) {
            Text line = Text.literal(entry.displayName())
                    .formatted(entry.rarity().getColor())
                    .append(Text.literal(" - " + entry.recordWeight() + " lbs, " + entry.recordLength() + " in")
                            .formatted(Formatting.YELLOW));

            context.drawTextWithShadow(this.textRenderer, line, x, lineY, 0xFFFFFF);
            lineY += 16;
        }
    }

    private void renderShinyPage(DrawContext context, int x, int y) {
        List<FishEntry> shinies = entries.stream()
                .filter(FishEntry::shiny)
                .toList();

        if (shinies.isEmpty()) {
            drawLine(context, "No shiny fish discovered yet.", x, y, Formatting.GRAY);
            return;
        }

        int lineY = y;

        for (FishEntry entry : shinies) {
            Text line = Text.literal("✦ ")
                    .formatted(Formatting.AQUA)
                    .append(Text.literal(entry.displayName()).formatted(entry.rarity().getColor()))
                    .append(Text.literal(" - " + entry.biome()).formatted(Formatting.DARK_AQUA));

            context.drawTextWithShadow(this.textRenderer, line, x, lineY, 0xFFFFFF);
            lineY += 14;
        }
    }

private void renderStatsPage(DrawContext context, int x, int y) {
    FishEntry mostCaught = entries.stream()
            .filter(FishEntry::discovered)
            .max(Comparator.comparingInt(FishEntry::catchCount))
            .orElse(null);

    FishEntry heaviest = entries.stream()
            .filter(FishEntry::discovered)
            .filter(entry -> entry.recordWeight() > 0)
            .max(Comparator.comparingDouble(FishEntry::recordWeight))
            .orElse(null);

    int leftX = x;
    int rightX = x + 155;

    drawLine(context, "ANGLER'S STATS", leftX, y, Formatting.GOLD);

    drawLine(context, "Species Found", leftX, y + 24, Formatting.GRAY);
    drawLine(context, discovered + " / " + totalFish, leftX, y + 38, Formatting.GREEN);

    drawLine(context, "Completion", rightX, y + 24, Formatting.GRAY);
    drawLine(context, completion + "%", rightX, y + 38, Formatting.YELLOW);

    drawLine(context, "Total Caught", leftX, y + 62, Formatting.GRAY);
    drawLine(context, String.valueOf(totalCatches), leftX, y + 76, Formatting.GOLD);

    drawLine(context, "Shiny Species", rightX, y + 62, Formatting.GRAY);
    drawLine(context, String.valueOf(shiny), rightX, y + 76, Formatting.AQUA);

    drawLine(context, "Trophy Catches", leftX, y + 100, Formatting.GRAY);

    drawLine(context, String.valueOf(trophies), leftX, y + 114, Formatting.LIGHT_PURPLE);

    drawLine(context, "Most Caught", rightX, y + 100, Formatting.GRAY);
    drawLine(
            context,
            mostCaught != null ? mostCaught.displayName() + " (" + mostCaught.catchCount() + ")" : "None yet",
            rightX,
            y + 114,
            mostCaught != null ? mostCaught.rarity().getColor() : Formatting.DARK_GRAY
    );

    drawLine(context, "Heaviest Catch", leftX, y + 138, Formatting.GRAY);

    if (heaviest != null) {
        drawLine(context, heaviest.displayName(), leftX, y + 152, heaviest.rarity().getColor());
        drawLine(context, String.format("%.1f lbs", heaviest.recordWeight()), leftX, y + 166, Formatting.YELLOW);
    } else {
        drawLine(context, "None yet", leftX, y + 152, Formatting.DARK_GRAY);
    }
}

    private void renderRodProgressionPage(DrawContext context, int x, int y) {
        drawLine(context, "DISCOVER FISH TO UPGRADE", x, y, Formatting.GREEN);
        drawLine(context, "Current tier: " + (rodTier == 0 ? "Vanilla Rod" : "Tier " + rodTier), x, y + 16, Formatting.AQUA);

        drawRodMilestone(context, x, y + 42, 1, 20, "Reed Rod", "64 durability | Steady Grip");
        drawRodMilestone(context, x, y + 70, 2, 50, "Oak Rod", "128 durability | Quick Set");
        drawRodMilestone(context, x, y + 98, 3, 100, "Ironwood Rod", "192 durability | Treasure Sense");
        drawRodMilestone(context, x, y + 126, 4, 150, "Angler's Rod", "256 durability | Master Angler");

        drawLine(context, "Advancements also announce each milestone.", x, y + 160, Formatting.GRAY);
    }

    private void drawRodMilestone(DrawContext context, int x, int y, int tier, int required, String name, String stats) {
        boolean unlocked = rodTier >= tier;
        drawLine(context, (unlocked ? "✓ " : "✗ ") + name + " — " + required + " species", x, y,
                unlocked ? Formatting.GREEN : Formatting.DARK_GRAY);
        drawLine(context, "  " + stats, x, y + 12, unlocked ? Formatting.YELLOW : Formatting.GRAY);
    }

    private void drawLine(DrawContext context, String text, int x, int y, Formatting formatting) {
        context.drawTextWithShadow(
                this.textRenderer,
                Text.literal(text).formatted(formatting),
                x,
                y,
                0xFFFFFF
        );
    }

    private int getMaxPage() {
        return 9;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

public record FishEntry(
        Identifier id,
        String displayName,
        FishRarity rarity,
        String biome,
        boolean discovered,
        boolean shiny,
        double recordWeight,
        int recordLength,
        int catchCount
) {
}
@Override
public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

    if (viewingFishDetails && keyCode == 256) { // ESC
        viewingFishDetails = false;
        selectedFish = null;
        return true;
    }

    return super.keyPressed(keyCode, scanCode, modifiers);
}
    @Override
public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double amount) {
    if (page >= 1 && page <= 5) {
        FishRarity rarity = switch (page) {
            case 1 -> FishRarity.COMMON;
            case 2 -> FishRarity.UNCOMMON;
            case 3 -> FishRarity.RARE;
            case 4 -> FishRarity.EPIC;
            case 5 -> FishRarity.LEGENDARY;
            default -> FishRarity.COMMON;
        };

        int totalRows = (int) entries.stream()
                .filter(entry -> entry.rarity() == rarity)
                .count();

        int maxVisibleRows = 10;
        int maxScroll = Math.max(0, totalRows - maxVisibleRows);

        if (amount < 0) {
            scrollOffset = Math.min(scrollOffset + 1, maxScroll);
        } else if (amount > 0) {
            scrollOffset = Math.max(scrollOffset - 1, 0);
        }

        return true;
    }

    return super.mouseScrolled(mouseX, mouseY, horizontalAmount, amount);
}
}
