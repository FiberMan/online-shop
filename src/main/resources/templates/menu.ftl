    <nav>
        <div class="nav nav-tabs">
            <!--nav_state: active | disabled-->
            <#if nav_state_products!="disabled"><a class="nav-item nav-link ${nav_state_products}" href="/products">Products</a></#if>
            <#if nav_state_product_add!="disabled"><a class="nav-item nav-link ${nav_state_product_add}" href="/product/add">Add product</a></#if>
            <#if nav_state_users!="disabled"><a class="nav-item nav-link ${nav_state_users}" href="/users">Users</a></#if>
            <#if nav_state_login!="disabled"><a class="nav-item nav-link ${nav_state_login}" href="/login">Log in</a></#if>
            <#if nav_state_logout!="disabled"><a class="nav-item nav-link ${nav_state_logout}" href="/logout">Log out</a></#if>
            <#if user_name!=""><span class="navbar-text" style="margin-left: auto;">Hi, <strong>${user_name}</strong></span></#if>

        </div>
    </nav>
    <br>
    <br>