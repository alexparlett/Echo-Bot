{%+ for namespace,plugins in docs.items() -%}
#### {{ namespace }}
| Action | Description | Examples |
| :----------------- |:------------------------ | :-----|
{%+ for plugin in plugins -%}
| {{ plugin.value() }} | {{ plugin.description() }} |  {% include 'templates/example.template.md' with context %} |
{%+ endfor -%}
{%+ endfor -%}