import React from 'react';
import Navbar from './components/Navbar';
import GroupSidebar from './components/GroupSidebar';
import NoteEditor from './components/NoteEditor';
import ToolsSidebar from './components/ToolsSidebar';

function App() {
  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-200">
      <Navbar />
      <div className="flex h-[calc(100vh-73px)]">
        <GroupSidebar />
        <NoteEditor />
        <ToolsSidebar />
      </div>
    </div>
  );
}

export default App;