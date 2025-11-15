import { useAuth } from '../contexts/AuthContext';
import { AuthModal } from './AuthModal';
import { useState } from 'react';

export const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  const [showAuthModal, setShowAuthModal] = useState(false);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!isAuthenticated) {
    return (
      <>
        <div className="flex items-center justify-center h-screen">
          <button 
            onClick={() => setShowAuthModal(true)}
            className="px-6 py-3 bg-blue-600 text-white rounded-lg"
          >
            Sign In to Continue
          </button>
        </div>
        <AuthModal 
          isOpen={showAuthModal} 
          onClose={() => setShowAuthModal(false)} 
        />
      </>
    );
  }

  return <>{children}</>;
};