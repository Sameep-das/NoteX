import { AuthProvider } from './contexts/AuthContext';
import { ThemeProvider } from './contexts/ThemeContext';
import { CanvasProvider } from './contexts/CanvasContext';
// import { ProtectedRoute } from './components/ProtectedRoute';/
import { ProtectedRoute } from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import GroupSidebar from './components/GroupSidebar';
import NoteEditor from './components/NoteEditor';
import ToolsSidebar from './components/ToolsSidebar';

function App() {
  return (
    <AuthProvider>
      <ThemeProvider>
        <CanvasProvider>
          <div className="min-h-screen bg-gray-50 dark:bg-gray-900">
            <Navbar />
            <ProtectedRoute>
              <div className="flex h-[calc(100vh-73px)]">
                <GroupSidebar />
                <NoteEditor />
                <ToolsSidebar />
              </div>
            </ProtectedRoute>
          </div>
        </CanvasProvider>
      </ThemeProvider>
    </AuthProvider>
  );
}

export default App;

// import React, { useState } from 'react';
// import { useAuth } from './contexts/AuthContext';
// import Login from './components/Login';
// import Register from './components/Register';
// import Navbar from './components/Navbar';
// import GroupSidebar from './components/GroupSidebar';
// import NoteEditor from './components/NoteEditor';
// import ToolsSidebar from './components/ToolsSidebar';

// function App() {
//   const { isAuthenticated, isLoading } = useAuth();
//   const [showRegister, setShowRegister] = useState(false);

//   if (isLoading) {
//     return (
//       <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900">
//         <div className="text-gray-600 dark:text-gray-400">Loading...</div>
//       </div>
//     );
//   }

//   if (!isAuthenticated) {
//     return showRegister ? (
//       <Register onSwitchToLogin={() => setShowRegister(false)} />
//     ) : (
//       <Login onSwitchToRegister={() => setShowRegister(true)} />
//     );
//   }

//   return (
//     <div className="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-200">
//       <Navbar />
//       <div className="flex h-[calc(100vh-73px)]">
//         <GroupSidebar />
//         <NoteEditor />
//         <ToolsSidebar />
//       </div>
//     </div>
//   );
// }

// export default App;