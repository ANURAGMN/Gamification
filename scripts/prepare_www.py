from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
src = ROOT / "EduAI_Master_Prototype_latest.html"
if not src.exists():
    src = ROOT / "EduAI_Master_Prototype.html"
text = src.read_text(encoding="utf-8")

text = text.replace(
    '<meta name="viewport" content="width=device-width, initial-scale=1.0">',
    '<meta name="viewport" content="width=device-width, initial-scale=1.0, viewport-fit=cover">',
)
text = text.replace(
    """body{
  margin:0; background:#E3E6EB; font-family:-apple-system,"Segoe UI",Roboto,Helvetica,Arial,sans-serif;
  display:flex; justify-content:center; align-items:flex-start; padding:28px 12px; min-height:100vh;
}""",
    """body{
  margin:0; background:var(--surface1); font-family:-apple-system,"Segoe UI",Roboto,Helvetica,Arial,sans-serif;
  display:flex; justify-content:stretch; align-items:stretch; padding:0; min-height:100vh; min-height:100dvh;
}""",
)
text = text.replace(
    ".device{width:380px; background:#111318; border-radius:42px; padding:12px; box-shadow:0 24px 48px rgba(20,24,34,0.28);}",
    ".device{width:100%; max-width:none; background:transparent; border-radius:0; padding:0; box-shadow:none; flex:1; display:flex;}",
)
text = text.replace(
    ".screen{background:var(--surface1); border-radius:32px; height:770px; display:flex; flex-direction:column; position:relative; overflow:hidden;}",
    ".screen{background:var(--surface1); border-radius:0; height:100vh; height:100dvh; min-height:100dvh; display:flex; flex-direction:column; position:relative; overflow:hidden;}",
)
text = text.replace(
    ".statusbar{height:20px;}",
    ".statusbar{height:env(safe-area-inset-top, 0px); min-height:env(safe-area-inset-top, 0px);}",
)
text = text.replace(
    ".bottomnav{display:flex; justify-content:space-around; align-items:center; padding:10px 0 16px; background:var(--surface2); border-top:0.5px solid var(--border); flex-shrink:0;}",
    ".bottomnav{display:flex; justify-content:space-around; align-items:center; padding:10px 0 calc(16px + env(safe-area-inset-bottom, 0px)); background:var(--surface2); border-top:0.5px solid var(--border); flex-shrink:0;}",
)
text = text.replace(
    "<title>EduAI — Master Prototype</title>",
    "<title>EduAI Gamification</title>",
)

targets = [
    ROOT / "www" / "index.html",
    ROOT / "android" / "app" / "src" / "main" / "assets" / "public" / "index.html",
]
for target in targets:
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text(text, encoding="utf-8")
    print(f"wrote {target}")
