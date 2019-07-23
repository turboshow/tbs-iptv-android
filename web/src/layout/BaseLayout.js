import React, { useState, useEffect } from 'react';
import { Layout, Menu } from 'antd';
import { withRouter } from 'react-router';
import { NavLink } from 'react-router-dom'
import logo from '../logo.png';

const { Header, Footer } = Layout;

function BaseLayout({ children, location }) {
  const [activeMenuItem, setActiveMenuItem] = useState();

  useEffect(() => {
    setActiveMenuItem(location.pathname.split('/')[1]);
  }, [location]);

  return (
    <Layout className="layout" style={{ minHeight: '100vh' }}>
      <Header style={{ padding: '0 0', height: '66px', backgroundColor: 'white', borderBottom: '1px solid #e8e8e8' }}>
        <div className="logo" style={{ float: 'left', fontSize: '18px', width: '200px', paddingLeft: '20px' }}>
          <img src={logo} style={{width: '18px', height: '18px'}} alt="" />
          土拨鼠影音
        </div>
        <Menu
          mode="horizontal"
          selectedKeys={[activeMenuItem]}
          style={{ lineHeight: '64px' }}
        >
          <Menu.Item key="settings"><NavLink to="/settings/playlist">设置</NavLink></Menu.Item>
          <Menu.Item key="about"><NavLink to="/about">关于</NavLink></Menu.Item>
        </Menu>
      </Header>
      {children}
      <Footer style={{ textAlign: 'center' }}>©2019 <a href="http://www.tvband.cn" target="_blank" rel="noopener noreferrer">土拨鼠</a></Footer>
    </Layout>
  );
}

export default withRouter(BaseLayout);