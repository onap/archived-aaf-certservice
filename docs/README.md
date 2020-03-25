## AAF Certification Service documentation

To generate a documentation locally follow below steps.

1. Install Sphinx (optional)

    You may already have sphinx sphinx installed – you can check by doing:
    ```
    python -c 'import sphinx'
   ```

    If that fails grab the latest version of and install it with:

    ```
    sudo easy_install -U Sphinx
    ```

    More information's you can find at
    ```
    https://matplotlib.org/sampledoc/getting_started.html#installing-your-doc-directory
    ```
2. Open **docs** folder in terminal
3. Prepare local environment
    ```
    make prepare
    ```
4. Generate local documentation
    ```
    make html
    ```
    After command execution the documentation will be available in **_build/html** folder.
5. Before you commit documentation changes please execute
    ```
    make clean
   ```
