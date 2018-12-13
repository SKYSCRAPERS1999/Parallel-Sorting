git filter-branch -f --prune-empty --index-filter 'git rm -rf --cached --ignore-unmatch 477a4544cb302ed9461a64918b8f428451411191' --tag-name-filter cat -- --all
